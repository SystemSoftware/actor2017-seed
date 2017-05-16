package org.asysob;

import java.io.UnsupportedEncodingException;

import org.zeromq.ZMQ;

public class Actor {
    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        System.out.println("Actor");

        if (args.length != 2) {
            System.out.println("Publisher:  pub <address>");
            System.out.println("            pub tcp://localhost:5432");
            System.out.println("Subscriber: sub <address publisher>");
            System.out.println("            sub tcp://localhost:5432");
            System.exit(-1);
        }

        switch (args[0].toLowerCase()) {
            case "pub":
                Publisher(args[1]);
                break;
            case "sub":
                Subscriber(args[1]);
                break;
            default:
                System.out.printf("Unkown mode %s\n",args[0]);
        }
    }

    public static void Publisher ( String socketAddress ) throws UnsupportedEncodingException {
        ZMQ.Context context = ZMQ.context(1);

        System.out.printf("Binding 0MQ PUB socket to <%s>\n",socketAddress);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind(socketAddress);

        int counter = 1;
        while (!Thread.currentThread().isInterrupted()) {
            Boolean counter_is_even = counter % 2 == 0;
            Message m = new Message();
            m.header.put("sender","me");
            m.body.put("number", Integer.toString(counter));
            m.body.put("even",Boolean.toString(counter_is_even));
            if (counter_is_even)
                publisher.sendMore("even");
            else
                publisher.sendMore("odd");
            publisher.send(m.toJSON().getBytes(),0);
            counter++;
        }

        publisher.close();
        context.term();
    }

    public static void Subscriber ( String serverAddress ) throws UnsupportedEncodingException {
        ZMQ.Context context = ZMQ.context(1);

        System.out.printf("Connecting to <%s>\n",serverAddress);
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect(serverAddress);
        subscriber.subscribe("even".getBytes());

        while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] data = subscriber.recv(0);
            String tags = new String(data,"UTF-8");
            System.out.printf("Tags: %s\n",tags);
            data = subscriber.recv(0);
            String js = new String(data,"UTF-8");
            System.out.printf("Message: %s\n",js);
            Message m = Message.fromJSON(js);
            System.out.printf("Number received was %d\n",Integer.parseInt(m.body.get("number")));
        }

        subscriber.close();
        context.term();
    }
}
