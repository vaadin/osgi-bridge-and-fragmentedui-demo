package org.vaadin.osgi.example.uifragment1;


import org.vaadin.osgi.bridge.uifragments.FragmentFactory;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class FragmentFactoryImpl1 implements FragmentFactory {

	@Override
	public Component getFragment() {
		VerticalLayout vl = new VerticalLayout();
		
		Property<Integer> numberOfClicks = new ObjectProperty<>(0);
		Label clickedTimes = new Label(numberOfClicks);
		
		vl.addComponent(new Button("Click me", new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7830684640567884545L;

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Thank you for clicking!");
				int oldNum = numberOfClicks.getValue();
				numberOfClicks.setValue(++oldNum);
			}
		}));
		
		vl.addComponent(clickedTimes);
		
		return vl;
	}

}
