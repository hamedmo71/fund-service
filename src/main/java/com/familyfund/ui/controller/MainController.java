package com.familyfund.ui.controller;

import com.familyfund.application.member.MemberService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    private final MemberService memberService;

    @FXML
    private Label messageLabel;

    public MainController(MemberService memberService) {
        this.memberService = memberService;
    }

    @FXML
    public void initialize() {

/*        messageLabel.setText(
                memberService.getMessage()
        );*/
    }

    @FXML
    private void handleTestButton() {

        messageLabel.setText(
                "Button clicked!"
        );
    }
}