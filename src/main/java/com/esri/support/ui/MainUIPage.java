package com.esri.support.ui;

import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import com.esri.support.messaging.AppKafkaProducerClient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("testapptheme")
public class MainUIPage extends UI {
	
	private AppKafkaProducerClient appKafkaClient = null; 

	@Override
	protected void init(VaadinRequest request) {
		appKafkaClient = AppKafkaProducerClient.getInstance(); 
		VerticalLayout layout = createMainPageLayout();
		setContent(layout);
	}
	
	private VerticalLayout createMainPageLayout() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		
		final TextArea firstChatArea = new TextArea("User Chat Entry");
		TextArea firstSubArea = new TextArea();
		TextArea secondSubArea = new TextArea();
		
		Button sendButton = new Button("Send Chat");
		sendButton.setClickShortcut(KeyCode.ENTER);
		sendButton.addClickListener(listener ->{ 
			String valueString = firstChatArea.getValue().trim();
			if(valueString.contains("firstTopic"))
			appKafkaClient.sendToKafkaProducer("firstTopic", firstChatArea.getValue().trim());
			else if(valueString.contains("secondTopic"))
				appKafkaClient.sendToKafkaProducer("secondTopic", firstChatArea.getValue().trim());
				
		});
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		VerticalLayout colOneVertLayout = new VerticalLayout();
		VerticalLayout colTwoVertLayout = new VerticalLayout();
		colOneVertLayout.setSizeFull();
		colTwoVertLayout.setSizeFull();
		setComponentSizes(firstChatArea, firstSubArea, secondSubArea, sendButton);
		colOneVertLayout.addComponents(firstChatArea, sendButton);
		colTwoVertLayout.addComponents(firstSubArea, secondSubArea);
		horizontalLayout.addComponents(colOneVertLayout, colTwoVertLayout);
		
		
		verticalLayout.addComponents(horizontalLayout);
		
		return verticalLayout;
	}

	
	
	private void setComponentSizes(Component... components) {
		Arrays.asList(components)
		.stream()
		.forEach(action -> {
			if(action instanceof TextArea) {
				action.setHeight("30%");
				action.setWidth("70%");
				action.setStyleName(ValoTheme.TEXTAREA_SMALL);
				action.setStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER, true);
			}
			
			
			if(action instanceof Button) {
				action.setStyleName(ValoTheme.BUTTON_PRIMARY);
				action.setStyleName(ValoTheme.BUTTON_SMALL, true);
			}
		});
		
	}
	
	
	@WebServlet(urlPatterns = "/*", name = "TestAppServlet", asyncSupported=true)
	@VaadinServletConfiguration(productionMode = false, ui = MainUIPage.class)
	private static class TestAppServlet extends VaadinServlet{
		
		
	}
}
