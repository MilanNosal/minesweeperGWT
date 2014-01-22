package org.tuke.nosal.minesweeper.client.ui.field;

import org.tuke.nosal.minesweeper.shared.SimpleNameValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NameDialog extends DialogBox implements HasText, HasClickHandlers {

	private static NameDialogUiBinder uiBinder = GWT
			.create(NameDialogUiBinder.class);

	interface NameDialogUiBinder extends UiBinder<Widget, NameDialog> {
	}

	private final SimpleNameValidator validator = new SimpleNameValidator();

	public NameDialog() {
		uiBinder.createAndBindUi(this);
	}

	@UiFactory
	DialogBox thatsJustMe() {
		// UiBinder will call this to get a DialogBox instance
		// and this is the DialogBox instance we want to use
		return this;
	}

	@UiField
	TextBox nameTextBox;

	@UiField
	Button nameButton;

	@UiField
	Button cancelButton;

	@UiHandler("nameTextBox")
	void writingStyle(FocusEvent event) {
		nameTextBox.removeStyleName(nameStyles.error());
		nameTextBox.removeStyleName(nameStyles.ok());
	}

	@UiHandler("nameTextBox")
	void checkValidityStyle(BlurEvent event) {
		if (validator.validateName(nameTextBox.getText())) {
			nameTextBox.addStyleName(nameStyles.ok());
		} else {
			nameTextBox.addStyleName(nameStyles.error());
		}
	}

	@UiHandler("cancelButton")
	void cancel(ClickEvent event) {
		this.hide();
	}

	@Override
	public String getText() {
		return nameTextBox.getText();
	}

	@Override
	public void setText(String text) {
		GWT.log("", new Exception("Text for NameDialogBox should not be set."));
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return this.nameButton.addClickHandler(handler);
	}
	
	@UiField NameStyle nameStyles;

	interface NameStyle extends CssResource {
		String ok();

		String error();
	}

}
