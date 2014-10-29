package org.vaadin.osgi.example;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = VaadinOSGiUI.class)
public class VaadinOSGiServlet extends VaadinServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7204996277655209911L;
}