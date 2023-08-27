package com.example.discordfx.controller;

import com.example.discordfx.domain.Message;
import com.example.discordfx.domain.friendship.Friendship;
import com.example.discordfx.domain.friendship.FriendshipState;
import com.example.discordfx.service.FriendshipService;
import com.example.discordfx.service.MessageService;
import com.example.discordfx.service.Service;
import com.example.discordfx.service.UserService;
import com.example.discordfx.service.dto.UserDto;
import com.example.discordfx.utils.javafx.LayoutHelper;
import com.example.discordfx.utils.javafx.node.MessagePane;
import com.example.discordfx.utils.javafx.node.MyContextMenu;
import com.example.discordfx.utils.javafx.node.MyMenuItem;
import com.example.discordfx.utils.javafx.node.UserPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

@SuppressWarnings("unchecked")
public class Logged extends Controller {

    @FXML
    public ListView<UserPane> listViewFriends, listViewRequests, listViewPendings, listViewPrivateMessages;
    @FXML
    private ObservableList<UserPane> friends, requests, pendings, openedDms;
    @FXML
    public ImageView imageViewNoFriends, imageViewNoPendings, imageViewNoRequests;
    @FXML
    public StackPane layoutAddFriend;
    @FXML
    public Button buttonFriendsLeft, buttonAddFriend, buttonRequests, buttonFriendsTop,
            buttonLogout, buttonSendFriendRequest, buttonSettings;
    @FXML
    public AnchorPane layoutLoggedUser;
    @FXML
    public UserPane userLabelLoggedUser;

    @FXML
    public TextField textFieldSearchDm, textFieldSearchMain, textFieldSearchAddFriend, textFieldSendMessage;
    @FXML
    public Label labelRequests, labelFriendUsername;
    @FXML
    public Pane layoutListViews, layoutMessages, layoutFriends, layoutPrivateMessage;
    @FXML
    public ScrollPane scrollPaneMessages;
    public static UserDto loggedUser;
    private UserDto openedDmUser;

    // Services
    private UserService userService;
    private FriendshipService fshipService;
    private MessageService messageService;

    // Maps
    private HashMap<Long, List<MessagePane>> privateMessages;

    // region Initializers

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> buttonFriendsLeft.requestFocus());
        friends = FXCollections.observableArrayList();
        requests = FXCollections.observableArrayList();
        pendings = FXCollections.observableArrayList();
        openedDms = FXCollections.observableArrayList();
        privateMessages = new HashMap<>();

        HBox parent = (HBox) textFieldSearchDm.getParent().getParent();
        disableChildrenFocusTraversable(parent, buttonFriendsLeft, textFieldSearchDm, textFieldSearchMain,
                textFieldSearchAddFriend);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        MyContextMenu friendsContextMenu = new MyContextMenu(),
                requestsContextMenu = new MyContextMenu(),
                pendingsContextMenu = new MyContextMenu();

        MyMenuItem itemOpenDm = new MyMenuItem("open dm", friendsContextMenu),
                itemRemove = new MyMenuItem("remove friend", friendsContextMenu),
                itemAccept = new MyMenuItem("accept", requestsContextMenu),
                itemDiscard = new MyMenuItem("discard", pendingsContextMenu);

        // Items Event Handlers
        itemOpenDm.setOnAction(this::handleMenuItemOpenDm);
        itemRemove.setOnAction(this::handleMenuItemRemove);
        itemAccept.setOnAction(this::handleMenuItemAccept);
        itemDiscard.setOnAction(this::handleMenuItemDiscardRequest);

        // Context Menu Items
        friendsContextMenu.getItems().addAll(itemOpenDm, itemRemove);
        requestsContextMenu.getItems().addAll(itemAccept);
        pendingsContextMenu.getItems().addAll(itemDiscard);

        initializeView(listViewFriends, friendsContextMenu);
        initializeView(listViewRequests, requestsContextMenu);
        initializeView(listViewPendings, pendingsContextMenu);
        initializeView(listViewPrivateMessages, null);
    }

    private void initializeView(ListView<UserPane> listView, MyContextMenu contextMenu) {
        listView.setCellFactory(lv -> {
            ListCell<UserPane> cell = new ListCell<>() {
                @Override
                protected void updateItem(UserPane item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null)
                        setGraphic(null);
                    else {
                        setGraphic(item);
                        if (contextMenu != null) {
                            contextMenu.setParent(this);
                            /*
                            For debug purposes

                            contextMenu.setOnShown(e -> {
                                System.out.println(getItem().labelUsername.getText());
                            });*/
                            setContextMenu(contextMenu);
                        }
                    }
                }

            };

            if (listView.equals(listViewFriends))
                cell.setOnMouseClicked(this::handleFriendCellPressed);
            else if (listView.equals(listViewPrivateMessages))
                cell.setOnMousePressed(this::handlePrivateMessagePressed);

            return cell;
        });
    }

    private void initializeListeners() {
        textFieldSearchAddFriend.lengthProperty().addListener((obs, oldValue, newValue) -> {
            buttonSendFriendRequest.setDisable((int) (newValue) <= 0);
        });
        layoutMessages.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            scrollPaneMessages.setVvalue((double) newHeight);
        });
    }

    // endregion

    // region Getters and Setters
    @Override
    public Service[] getServices() {
        return new Service[]{userService, fshipService, messageService};
    }

    @Override
    public void setServices(Service... service) {
        try {
            userService = (UserService) service[0];
        } catch (Exception ignored) {
            userService = new UserService();
        }

        try {
            fshipService = (FriendshipService) service[1];
        } catch (Exception ignored) {
            fshipService = new FriendshipService();
        }

        try {
            messageService = (MessageService) service[2];
        }
        catch (Exception ignored){
            messageService = new MessageService();
        }

        load();
    }

    public void setLoggedUser(UserDto user) {
        loggedUser = user;
    }

    public UserDto getLoggedUser() {
        return loggedUser;
    }

    // endregion

    // region Event Handlers
    @FXML
    public void handleRequests() {
        hideImageViews();
        listViewFriends.setVisible(false);
        listViewRequests.setVisible(true);
        listViewPendings.setVisible(false);

        layoutListViews.setVisible(true);
        layoutAddFriend.setVisible(false);

        if (requests.size() == 0) {
            layoutListViews.setVisible(false);
            imageViewNoRequests.setVisible(true);
        }
    }

    @FXML
    public void handleFriends() {
        hideImageViews();
        listViewFriends.setVisible(true);
        listViewRequests.setVisible(false);
        listViewPendings.setVisible(false);

        layoutListViews.setVisible(true);
        layoutAddFriend.setVisible(false);

        if (friends.size() == 0) {
            layoutListViews.setVisible(false);
            imageViewNoFriends.setVisible(true);
        }
    }

    @FXML
    public void handleAddFriend() {
        hideImageViews();
        listViewFriends.setVisible(false);
        listViewRequests.setVisible(false);
        listViewPendings.setVisible(true);

        layoutListViews.setVisible(false);
        layoutAddFriend.setVisible(true);

        if (pendings.size() == 0) {
            listViewPendings.setVisible(false);
            imageViewNoPendings.setVisible(true);
        }
    }

    @FXML
    public void handleLogout() throws IOException {
        URL url = getClass().getResource("/com/example/discordfx/fxml/stage/Login.fxml");
        Stage currentStage = getStage();
        changeStage(currentStage, url);
    }

    @FXML
    public void handleSendFriendRequest() {
        String usernameAndTag = textFieldSearchAddFriend.getText();

        Label labelWarning = new Label();
        labelWarning.setId("labelWarningOnFriendRequest");
        labelWarning.setStyle("-fx-font-family: Calibri; ");

        boolean isValidInput = usernameAndTag.matches("[a-zA-Z0-9]* ?[a-zA-Z0-9]*#[0-9]{4}");
        if (!isValidInput) {
            labelWarning.setText("Invalid input! Make sure you provide both the username and the tag.");
            labelWarning.setStyle("-fx-text-fill: RED; ");
        } else {
            String tag = usernameAndTag.split("#")[1];
            long userTag = Long.parseLong(tag);
            UserDto user = userService.get(userTag);
            if (user != null && !user.equals(loggedUser)) {
                Friendship f = fshipService.get(loggedUser.getId(), userTag);
                if (f != null) {
                    if (f.getState() == FriendshipState.ACCEPTED)
                        labelWarning.setText("Already friends!");
                    else
                        labelWarning.setText("Already sent a friend request to this user before!");
                    labelWarning.setStyle("-fx-text-fill: YELLOW");
                } else {
                    fshipService.add(loggedUser.getId(), userTag);
                    pendings.add(0, new UserPane(user, true));

                    handleAddFriend();
                }
            } else if (user != null && user.equals(loggedUser)) {
                labelWarning.setText("I'm certain that you can be your own friend... just not in this place.");
                labelWarning.setStyle("-fx-text-fill: RED; ");
            } else {
                labelWarning.setText("User doesn't exist!");
                labelWarning.setStyle("-fx-text-fill: RED; ");
            }
        }

        VBox vBox = (VBox) layoutAddFriend.getChildren().get(0);
        Node child = vBox.getChildren().get(3);
        if (labelWarning.getText().length() > 0) {
            LayoutHelper.addLabel(labelWarning, 3, vBox);
        } else if (LayoutHelper.isLabel(child) && Objects.equals(child.getId(), "labelWarningOnFriendRequest")) {
            vBox.getChildren().remove(3);
        }
    }

    @FXML
    public void handleTextFieldSearchAddFriendKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !buttonSendFriendRequest.isDisabled()) {
            handleSendFriendRequest();
        }
    }

    @FXML
    public void handleButtonFriendsLeftClicked(MouseEvent mouseEvent) {
        layoutFriends.setVisible(true);
        layoutPrivateMessage.setVisible(false);
    }

    @FXML
    public void handleGlobalMouseClicked(MouseEvent mouseEvent) {
        buttonFriendsLeft.requestFocus();
    }

    @FXML
    public void handleMessageSenderKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            String message = textFieldSendMessage.getText();
            if (!message.isEmpty()){
                handleSendMessage(message);
                textFieldSendMessage.clear();
            }
        }
    }

    private void handleMenuItemAccept(ActionEvent e) {
        MyMenuItem menuItem = (MyMenuItem) e.getSource();
        MyContextMenu menu = menuItem.getParent();
        ListCell<UserPane> cell = (ListCell<UserPane>) menu.getParent();
        UserPane userPane = cell.getItem();

        long userTag = Long.parseLong(userPane.labelId.getText().replace("#", ""));
        fshipService.update(userTag, loggedUser.getId(), FriendshipState.ACCEPTED);
        requests.remove(userPane);
        handleRequests();
        refreshRequestsLabel();

        friends.add(0, new UserPane(userService.get(userTag), true));
    }

    private void handleMenuItemRemove(ActionEvent e) {
        MyMenuItem menuItem = (MyMenuItem) e.getSource();
        MyContextMenu menu = menuItem.getParent();
        ListCell<UserPane> cell = (ListCell<UserPane>) menu.getParent();
        UserPane userPane = cell.getItem();

        long userTag = Long.parseLong(userPane.labelId.getText().replace("#", ""));
        fshipService.remove(loggedUser.getId(), userTag);
        friends.remove(userPane);
        handleFriends();
    }

    private void handleMenuItemOpenDm(ActionEvent e) {
        MyMenuItem menuItem = (MyMenuItem) e.getSource();
        MyContextMenu menu = menuItem.getParent();
        ListCell<UserPane> cell = (ListCell<UserPane>) menu.getParent();

        MouseEvent mouseEvent = generateMouseEvent(cell, null, MouseButton.PRIMARY);
        handleFriendCellPressed(mouseEvent);
    }

    private void handleMenuItemDiscardRequest(ActionEvent e) {
        MyMenuItem menuItem = (MyMenuItem) e.getSource();
        MyContextMenu menu = menuItem.getParent();
        ListCell<UserPane> cell = (ListCell<UserPane>) menu.getParent();
        UserPane userPane = cell.getItem();

        long userTag = Long.parseLong(userPane.labelId.getText().replace("#", ""));
        fshipService.remove(loggedUser.getId(), userTag);
        pendings.remove(userPane);
        handleAddFriend();
    }

    private void handleFriendCellPressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ListCell<UserPane> cell = (ListCell<UserPane>) mouseEvent.getSource();
            UserPane userPane = cell.getItem();
            if (userPane == null)
                return;

            UserDto user = userPane.getUser();

            boolean contained = false;
            for (UserPane pm : openedDms) {
                if (pm.getUser().getId() == user.getId()) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                openedDms.add(new UserPane(user, false));
            }

            MouseEvent _mouseEvent = generateMouseEvent(cell, null, MouseButton.PRIMARY);
            handlePrivateMessagePressed(_mouseEvent);
        }
    }

    private void handlePrivateMessagePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ListCell<UserPane> cell = (ListCell<UserPane>) mouseEvent.getSource();
            UserPane userPane = cell.getItem();
            if (userPane == null)
                return;

            UserDto user = userPane.getUser();
            openedDmUser = user;

            String username = user.getFirst() + " " + user.getLast();
            labelFriendUsername.setText(username);
            textFieldSendMessage.setPromptText("Send message on @" + username);

            handleLoadConversation(user);
            layoutFriends.setVisible(false);
            layoutPrivateMessage.setVisible(true);
        }
    }

    private void handleSendMessage(String message){
        int messagePaneNo = layoutMessages.getChildren().size();
        Image image = new Image("/com/example/discordfx/image/" + loggedUser.getPictureId());
        String username = loggedUser.getFirst() + " " + loggedUser.getLast();

        MessagePane messagePane = new MessagePane(image, username, loggedUser.getId());
        if (messagePaneNo > 0){
            MessagePane lastInsertedMessagePane = (MessagePane) layoutMessages.getChildren().get(messagePaneNo-1);
            if (lastInsertedMessagePane.getUserId() == loggedUser.getId()){
                messagePane = lastInsertedMessagePane;
            }
            else{
                layoutMessages.getChildren().add(messagePane);
            }
        }
        else {
            layoutMessages.getChildren().add(messagePane);
        }

        messagePane.addMessage(message);
        messageService.add(message, loggedUser.getId(), openedDmUser.getId());
    }

    private void handleLoadConversation(UserDto friend){
        layoutMessages.getChildren().removeAll(layoutMessages.getChildren());

        long friendId = friend.getId();
        if (privateMessages.containsKey(friend.getId())){
            for(MessagePane messagePane: privateMessages.get(friendId)){
                layoutMessages.getChildren().add(messagePane);
            }
        }
        else {
            for (Message message : messageService.getConversation(loggedUser.getId(), friendId)) {
                long fromUserId = message.getFromUserId();

                UserDto user = userService.get(fromUserId);
                Image image = new Image(String.valueOf(getClass().getResource("/com/example/discordfx/image/" + user.getPictureId())));
                String username = user.getFirst() + " " + user.getLast();

                MessagePane messagePane = new MessagePane(image, username, fromUserId);

                int messagePaneNo = layoutMessages.getChildren().size();
                if (messagePaneNo > 0) {
                    MessagePane lastInsertedMessagePane = (MessagePane) layoutMessages.getChildren().get(messagePaneNo - 1);
                    if (lastInsertedMessagePane.getUserId() == fromUserId) {
                        messagePane = lastInsertedMessagePane;
                    } else {
                        layoutMessages.getChildren().add(messagePane);
                        privateMessages.get(friendId).add(messagePane);
                    }
                } else {
                    layoutMessages.getChildren().add(messagePane);
                    List<MessagePane> messagePanes = new ArrayList<>();
                    messagePanes.add(messagePane);
                    privateMessages.put(friendId, messagePanes);
                }

                String text = message.getText();
                messagePane.addMessage(text);
            }
        }
    }

    // endregion

    // region Loaders

    private void load() {
        changeDisplay(2);
        loadDataFromServices();
        refreshLoggedUserLabel();
        refreshRequestsLabel();
    }

    private void loadDataFromServices() {
        getFriendsByState(FriendshipState.ACCEPTED).forEach(f -> {
            friends.add(new UserPane(f, true));
        });
        for (UserDto user : getFriendsByState(FriendshipState.PENDING)) {
            Friendship f = fshipService.get(loggedUser.getId(), user.getId());
            if (f.getU1() != loggedUser.getId()) {
                requests.add(new UserPane(user, true));
            } else {
                pendings.add(new UserPane(user, true));
            }
        }

        listViewFriends.setItems(friends);
        listViewRequests.setItems(requests);
        listViewPendings.setItems(pendings);
        listViewPrivateMessages.setItems(openedDms);

        handleFriends();
    }

    // endregion

    private void hideImageViews() {
        imageViewNoFriends.setVisible(false);
        imageViewNoRequests.setVisible(false);
        imageViewNoPendings.setVisible(false);
    }

    private void refreshLoggedUserLabel() {
        UserDto loggedUser = getLoggedUser();
        userLabelLoggedUser.setUser(loggedUser);
    }

    private void refreshRequestsLabel() {
        if (requests.size() > 0) {
            labelRequests.setText(String.valueOf(requests.size()));
            labelRequests.setVisible(true);
        } else {
            labelRequests.setVisible(false);
        }
    }

    private Iterable<UserDto> getFriendsByState(FriendshipState state) {
        List<UserDto> result = new ArrayList<>();
        for (Long userId : fshipService.getFriendsIds(loggedUser.getId(), state)) {
            UserDto user = userService.get(userId);
            result.add(user);
        }
        return result;
    }

    /**
     * Disables focus traversable propriety for all children nodes of a given node, except for those found in the ignored array.
     *
     * @param parent  The parent node (this node will not be affected)
     * @param ignored The array list containing that nodes to be ignored (the focus traversable propriety for these nodes will not be affected)
     */
    private void disableChildrenFocusTraversable(Node parent, Node... ignored) {
        if (parent == null)
            return;

        try {
            for (Node child : (ObservableList<Node>) parent.getClass().getMethod("getChildren").invoke(parent)) {
                if (!Arrays.asList(ignored).contains(child)) {
                    child.setFocusTraversable(false);
                }
                disableChildrenFocusTraversable(child, ignored);
            }
            ;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignoredException) {
        }
    }

    private MouseEvent generateMouseEvent(Node source, Node target, MouseButton mouseButton) {
        return new MouseEvent(source, target, null, 0, 0, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
    }
}
