/*
 * Copyright (c) 2016, 2018 Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.cloudlink.sample.whiteboard.views;

import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.animation.BounceInLeftTransition;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.cloudlink.sample.whiteboard.Whiteboard;
import com.gluonhq.cloudlink.sample.whiteboard.model.Item;
import com.gluonhq.cloudlink.sample.whiteboard.model.Model;
import com.gluonhq.cloudlink.sample.whiteboard.service.Service;
import com.gluonhq.connect.GluonObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.inject.Inject;

import static com.gluonhq.cloudlink.sample.whiteboard.views.AppViewManager.EDITION_VIEW;

public class BoardPresenter extends GluonPresenter<Whiteboard> {

    @Inject private Service service;
    @Inject private Model model;

    @FXML private View board;

    @FXML private ListView<Item> lstItems;

    public void initialize() {
        board.setShowTransitionFactory(BounceInLeftTransition::new);
        board.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                AppBar appBar = getApp().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(event -> getApp().getDrawer().open()));
                appBar.setTitleText("Whiteboard");
            }
        });

        final FloatingActionButton floatingActionButton = new FloatingActionButton();
        floatingActionButton.setOnAction(e -> edit(null));
        floatingActionButton.showOn(board);

        GluonObservableList<Item> items = service.retrieveItems();
        lstItems.setItems(items);
        lstItems.setCellFactory(p -> new ItemCell(service, this::edit, this::remove));
        lstItems.setPlaceholder(new Label("The whiteboard is empty."));

    }

    private void edit(Item item) {
        model.activeItem().set(item);
        EDITION_VIEW.switchView();
    }

    private void remove(Item item) {
        model.getItems().remove(item);
    }
}
