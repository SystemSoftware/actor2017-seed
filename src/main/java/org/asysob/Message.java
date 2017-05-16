package org.asysob;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Message {
    public Map<String,String> header;
    public Map<String,String> body;

    public Message ( ) {
        header = new HashMap<String,String>(3);
        body = new HashMap<String,String>();
    }

    public String toJSON () {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Message fromJSON ( String js ) {
        Gson gson = new Gson();
        return gson.fromJson(js,Message.class);
    }

    public byte[] toByteArray () {
        return toJSON().getBytes();
    }

    public static Message fromByteArray ( byte[] data ) {
        String srep;
        try {
            srep = new String(data, "UTF-8");
        }
        catch ( UnsupportedEncodingException e) {
            return null;
        }
        return fromJSON(srep);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Message: Header=<");
        for (Map.Entry<String,String> entry : header.entrySet() ) {
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(entry.getValue());
            sb.append(' ');
        }
        sb.append("> Body=<");
        for (Map.Entry<String,String> entry : body.entrySet() ) {
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(entry.getValue());
            sb.append(' ');
        }
        sb.append(">");
        return sb.toString();
    }
}