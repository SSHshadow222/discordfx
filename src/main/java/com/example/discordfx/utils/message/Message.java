package com.example.discordfx.utils.message;

public class Message
{

    public static javafx.scene.paint.Color getColorByState(MessageState state){
        if (state == MessageState.NORMAL || state == MessageState.REGULAR){
            return Color.WHITE;
        }
        else if (state == MessageState.WARNING){
            return Color.YELLOW;
        }
        else{   // ERROR MESSAGE
            return Color.RED;
        }
    }
}