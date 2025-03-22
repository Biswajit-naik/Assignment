package com.expenses.tracker.ExpensesTracker.utils;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.expenses.tracker.ExpensesTracker.Controller.ExpensesController;
import com.expenses.tracker.ExpensesTracker.Model.Expensemodel;

@Component
public class ExpensemodelAssembler implements RepresentationModelAssembler<Expensemodel, EntityModel<Expensemodel>> {

    @Override
    public EntityModel<Expensemodel> toModel(Expensemodel expense) {
        // Create a self-link to the individual expense resource
        return EntityModel.of(expense,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class)
                .getExpenseById(expense.getExpenseId())).withSelfRel());
    }
}