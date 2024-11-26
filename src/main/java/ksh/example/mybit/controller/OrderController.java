package ksh.example.mybit.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.controller.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    @PostMapping("/order")
    public void postOrder(@Valid @ModelAttribute OrderForm orderForm){

    }
}
