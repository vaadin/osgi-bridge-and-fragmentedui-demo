package org.vaadin.osgi.example;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.vaadin.osgi.bridge.uifragments.FragmentFactory;

public class VaadinActivator implements BundleActivator,
		ServiceTrackerCustomizer<FragmentFactory, FragmentFactory> {
	
	private static final Logger log = Logger.getLogger(VaadinActivator.class.getCanonicalName());
	
	private static final List<FragmentFactory> factories = new ArrayList<>(); //not sure if it should be done this way…

	static BundleContext context;
	HttpService httpService;

	ServiceTracker<FragmentFactory, FragmentFactory> fragmentFactoriesTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		VaadinActivator.context = context;

		fragmentFactoriesTracker = new ServiceTracker<>(
				context, FragmentFactory.class, this);
		fragmentFactoriesTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		fragmentFactoriesTracker.close();
		context = null;
	}

	@Override
	public FragmentFactory addingService(
			ServiceReference<FragmentFactory> reference) {
		FragmentFactory ff = context.getService(reference);
		
		log.log(Level.ALL, "Registering FragmentFactory service {0}", ff);
		if(!factories.contains(ff)) {
			factories.add(ff);
		}
		
		return ff;
	}

	@Override
	public void modifiedService(ServiceReference<FragmentFactory> reference,
			FragmentFactory service) {
		
	}

	@Override
	public void removedService(ServiceReference<FragmentFactory> reference,
			FragmentFactory ff) {
		log.log(Level.ALL, "De-registering FragmentFactory service {0}", ff);
		factories.remove(ff);
	}
}
