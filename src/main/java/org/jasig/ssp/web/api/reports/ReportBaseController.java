/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.service.EarlyAlertResponseService;
import org.jasig.ssp.transferobject.reports.BaseStudentReportTO;
import org.jasig.ssp.transferobject.reports.EarlyAlertResponseCounts;
import org.jasig.ssp.transferobject.reports.EarlyAlertStudentReportTO;
import org.jasig.ssp.util.csvwriter.AbstractCsvWriterHelper;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.web.api.AbstractBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

abstract class ReportBaseController<R> extends AbstractBaseController {

	public static final String REPORT_TYPE_PDF = "pdf";
	public static final String REPORT_TYPE_CSV = "csv";
	public static final String DEFAULT_REPORT_TYPE = REPORT_TYPE_PDF;
	
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportBaseController.class);
	private static final String DEFAULT_REPORT_NAME = "report"; // no 'ssp' prefix b/c that's branding

	@Autowired
	private ServletContext servletContext;

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	protected void renderReport(HttpServletResponse response, Map<String, Object> reportParameters,
								  Collection<R> reportResults, String reportViewUrl, String reportType,
								  String reportName)
			throws IOException {
		reportName = normalizeReportName(reportName);
		// TODO this should really all be shunted off to Spring's View mechanism
		if (REPORT_TYPE_PDF.equals(reportType)) {
			renderPdfReport(response, reportParameters, reportResults, reportViewUrl, reportType, reportName);
		} else if ( REPORT_TYPE_CSV.equals(reportType) ) {
			renderCsvReport(response, reportParameters, reportResults, reportViewUrl, reportType, reportName);
		} else {
			throw new IllegalArgumentException("Unrecognized report type");
		}
	}

	protected String normalizeReportName(String reportName) {
		if (StringUtils.isBlank(reportName)) {
			return DEFAULT_REPORT_NAME;
		}
		return reportName.replaceAll("\\s", "_");
	}

	protected void renderPdfReport(HttpServletResponse response, Map<String, Object> reportParameters,
								   Collection<R> reportResults, String reportViewUrl, String reportType,
								   String reportName)
			throws IOException {
		try {
			renderJasperReport(response, reportParameters, reportResults, reportViewUrl, reportType, reportName);
		} catch ( JRException e ) {
			throw new RuntimeException(e);
		}
	}

	protected void renderCsvReport(HttpServletResponse response, Map<String, Object> reportParameters,
								   Collection<R> reportResults, String reportViewUrl, String reportType,
								   String reportName)
			throws IOException {
		if ( overridesCsvRendering() ) {
			renderCsvReportWithFromattingOverrides(response, reportParameters, reportResults, reportViewUrl, reportType, reportName);
		} else {
			try {
				renderJasperReport(response, reportParameters, reportResults, reportViewUrl, reportType, reportName);
			} catch ( JRException e ) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected void renderJasperReport(HttpServletResponse response, Map<String, Object> reportParameters,
									  Collection<R> reportResults, String reportViewUrl, String reportType,
									  String reportName) throws JRException, IOException{

		SearchParameters.addReportDateToMap(reportParameters);
		final InputStream is = getClass().getResourceAsStream(reportViewUrl);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		
		JRDataSource beanDS;
		if (reportResults == null || reportResults.size() <= 0) {
			beanDS = new JREmptyDataSource();
		} else {
			beanDS = new JRBeanCollectionDataSource(reportResults);
		}

		if (REPORT_TYPE_PDF.equals(reportType)) {
			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.default.font.name", "DejaVu Sans");
			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.default.pdf.font.name", "DejaVu Sans");

			if ( !(reportParameters.containsKey("realPath")) ) {
				reportParameters.put("realPath", servletContext.getRealPath("/"));
			}
		}
		
		JasperFillManager.fillReportToStream(is, os, reportParameters, beanDS);
		final InputStream decodedInput = new ByteArrayInputStream(
				os.toByteArray());

		if (REPORT_TYPE_PDF.equals(reportType)) {
			response.setHeader(
					"Content-disposition",
					"attachment; filename=" + reportName + "." + REPORT_TYPE_PDF);
			JasperExportManager.exportReportToPdfStream(decodedInput,
					response.getOutputStream());
		} else if ("csv".equals(reportType)) {
			writeCsvHttpResponseHeaders(response, reportName);

			final JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.INPUT_STREAM,
					decodedInput);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					response.getOutputStream());
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);

			exporter.exportReport();
		}
		
		response.flushBuffer();
		is.close();
		os.close();		
	}

	/**
	 * If {@code true}, subclasses will have the opportunity to participate in more fine-grained, i.e. non-Jasper CSV
	 * report rendering. In particular, CSV rendering will rely on
	 * {@link #csvBodyRow(Object, java.util.Map, java.util.Collection, String, String, String, org.jasig.ssp.util.csvwriter.AbstractCsvWriterHelper)}
	 * and
	 * {@link #csvBodyRow(Object, java.util.Map, java.util.Collection, String, String, String, org.jasig.ssp.util.csvwriter.AbstractCsvWriterHelper)}
	 * via {@link #renderCsvReportWithFromattingOverrides(javax.servlet.http.HttpServletResponse, java.util.Map, java.util.Collection, String, String, String)}.
	 * This is designed with a flag method rather than a mixin interface b/c Spring will not map {@code Controllers} that
	 * mix in interfaces unless we change our proxying mode (See <a href="http://stackoverflow.com/a/16970812">this SO
	 * answer</a>)
	 */
	protected boolean overridesCsvRendering() {
		return false;
	}

	protected void renderCsvReportWithFromattingOverrides(final HttpServletResponse response,
														  final Map<String, Object> reportParameters,
														  final Collection<R> reportResults, final String reportViewUrl,
														  final String reportType, final String reportName)
			throws IOException {
		writeCsvHttpResponseHeaders(response, reportName);
		AbstractCsvWriterHelper<R> csvWriter = new AbstractCsvWriterHelper<R>(response.getWriter()) {
			@Override
			protected String[] csvHeaderRow() {
				return ReportBaseController.this
						.csvHeaderRow(reportParameters, reportResults, reportViewUrl, reportType, reportName, this);
			}

			@Override
			protected List<String[]> csvBodyRows(R model) {
				return ReportBaseController.this
						.csvBodyRows(model, reportParameters, reportResults, reportViewUrl, reportType, reportName, this);
			}
		};
		csvWriter.write((Collection<R>)reportResults, -1L);
	}

	/** Defaults to an angry no-op. See {@link #overridesCsvRendering()}. Should not be called unless that op returns
	 * true */
	protected String[] csvHeaderRow(Map<String, Object> reportParameters,
											 Collection<R> reportResults, String reportViewUrl,
											 String reportType, String reportName,
											 AbstractCsvWriterHelper csvHelper) {
		throw new UnsupportedOperationException();
	}

	/** Defaults to an angry no-op. See {@link #overridesCsvRendering()}. Should not be called unless that op returns
	 * true */
	protected List<String[]> csvBodyRows(R reportResultElement,
										   Map<String, Object> reportParameters,
										   Collection<R> reportResults, String reportViewUrl,
										   String reportType, String reportName,
										   AbstractCsvWriterHelper csvHelper) {
		throw new UnsupportedOperationException();
	}

	protected void writeCsvHttpResponseHeaders(HttpServletResponse response, String reportName) {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader(
				"Content-disposition",
				"attachment; filename=" + reportName + "." + REPORT_TYPE_CSV);
	}

	<T extends BaseStudentReportTO> List<T> processStudentReportTOs(PagingWrapper<T> people){
		if(people == null || people.getRows().size() <= 0)
			return new ArrayList<T>();
		 return processStudentReportTOs(new ArrayList<T>(people.getRows()));
	}

	<T extends BaseStudentReportTO> List<T> processStudentReportTOs(Collection<T> reports){
		ArrayList<T> compressedReports = new ArrayList<T>();
		if(reports == null || reports.size() <= 0)
			return compressedReports;
		
		for(T reportTO: reports){
			Integer index = compressedReports.indexOf(reportTO);
			if(index != null && index >= 0)
			{
				T compressedReportTo = compressedReports.get(index);
				compressedReportTo.processDuplicate(reportTO);
			}else{
				reportTO.normalize();
				compressedReports.add(reportTO);
			}
		}
		return compressedReports;
	}
	
	protected List<EarlyAlertStudentReportTO> processReports(PagingWrapper<EarlyAlertStudentReportTO> reports, EarlyAlertResponseService earlyAlertResponseService){
		 
		ArrayList<EarlyAlertStudentReportTO> compressedReports = new ArrayList<EarlyAlertStudentReportTO>();
		if(reports == null || reports.getRows().size() <= 0)
			return compressedReports;
		
		for(EarlyAlertStudentReportTO reportTO: reports){
			Integer index = compressedReports.indexOf(reportTO);
			if(index != null && index >= 0)
			{
				BaseStudentReportTO compressedReportTo = compressedReports.get(index);
				compressedReportTo.processDuplicate(reportTO);
			}else{
				reportTO.normalize();
				compressedReports.add(reportTO);
			}
		}
		
		for(EarlyAlertStudentReportTO reportTO: compressedReports){
			EarlyAlertResponseCounts countOfResponses = earlyAlertResponseService.getCountEarlyAlertRespondedToForEarlyAlerts(reportTO.getEarlyAlertIds());
			reportTO.setPending(countOfResponses.getTotalEARespondedToNotClosed());
		}
		return compressedReports;
	}

}
