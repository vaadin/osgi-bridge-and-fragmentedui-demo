package org.vaadin.osgi.example.uifragment1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.vaadin.osgi.bridge.uifragments.FragmentFactory;

public class FragmentFactory1Activator implements BundleActivator {

	private ServiceRegistration<FragmentFactory> serviceRegistration;
	private FragmentFactoryImpl1 service;

	@Override
	public void start(BundleContext context) throws Exception {
		service = new FragmentFactoryImpl1();
		serviceRegistration = context.registerService(FragmentFactory.class, service, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
