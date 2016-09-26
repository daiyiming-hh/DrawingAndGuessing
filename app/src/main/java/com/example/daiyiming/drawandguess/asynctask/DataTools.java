package com.example.daiyiming.drawandguess.asynctask;

import com.example.daiyiming.drawandguess.constants.Command;
import com.example.daiyiming.drawandguess.view.Point;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiyiming on 2016/9/14.
 */

public class DataTools {

    private final static String KEY_X = "x";
    private final static String KEY_Y = "y";
    private final static String KEY_COMMAND = "command";
    private final static String KEY_POINTS = "points";
    private final static String KEY_MESSAGE = "message";

    public static MessageHolder parse(String data) {
        MessageHolder holder = null;
        int command = Command.NONE;
        List<Point> list = null;
        String message = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            command = jsonObject.getInt(KEY_COMMAND);
            switch (command) {
                case Command.DRAW: {
                    list = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_POINTS);
                    for (int i = 0; i < jsonArray.length(); i ++) {
                        JSONObject pointJson = jsonArray.getJSONObject(i);
                        Point point = Point.create((float) pointJson.getDouble(KEY_X), (float) pointJson.getDouble(KEY_Y));
                        list.add(point);
                    }
                }
                break;
                case Command.CLEAR: {

                }
                break;
                case Command.MESSAGE: {
                    message = jsonObject.getString(KEY_MESSAGE);
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder = new MessageHolder(command, list, message);
        return holder;
    }

    public static String compileDrawData(List<Point> list) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_COMMAND, Command.DRAW);
            JSONArray array = new JSONArray();
            for (Point point : list) {
                JSONObject pointJson = new JSONObject();
                pointJson.put(KEY_X, point.x);
                pointJson.put(KEY_Y, point.y);
                array.put(pointJson);
            }
            jsonObject.put(KEY_POINTS, array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String compileMessageData(String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_COMMAND, Command.MESSAGE);
            jsonObject.put(KEY_MESSAGE, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String compileClearData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_COMMAND, Command.CLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
