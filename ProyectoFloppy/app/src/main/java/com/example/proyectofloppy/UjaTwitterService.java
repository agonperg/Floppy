package com.example.proyectofloppy;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UjaTwitterService {

    public interface TwitterCallback {
        void onTweetsLoaded(List<Tweet> tweets);
    }

    public static class Tweet {
        public String content;
        public String link;
        public String date;

        public Tweet(String content, String link, String date) {
            this.content = content;
            this.link = link;
            this.date = date;
        }
    }

    public static void fetchLatestTweets(TwitterCallback callback) {
        new Thread(() -> {
            List<Tweet> tweets = new ArrayList<>();
            try {
                // Usamos un mirror de Nitter que ofrece RSS gratuito de cualquier cuenta de X
                URL url = new URL("https://nitter.net/ujaen/rss");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                InputStream is = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(is, "UTF-8");

                int eventType = parser.getEventType();
                Tweet currentTweet = null;
                String text = "";

                while (eventType != XmlPullParser.END_DOCUMENT && tweets.size() < 3) {
                    String name = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                currentTweet = new Tweet("", "", "");
                            }
                            break;

                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if (currentTweet != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    // El título del RSS suele ser el contenido del tweet
                                    currentTweet.content = text.replace("R to @ujaen: ", "").trim();
                                } else if (name.equalsIgnoreCase("link")) {
                                    currentTweet.link = text.replace("nitter.net", "x.com"); // Convertimos de vuelta a X
                                } else if (name.equalsIgnoreCase("pubDate")) {
                                    currentTweet.date = text;
                                } else if (name.equalsIgnoreCase("item")) {
                                    tweets.add(currentTweet);
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                Log.e("TwitterService", "Error fetching RSS", e);
            }

            // Volver al hilo principal para actualizar la UI
            new Handler(Looper.getMainLooper()).post(() -> callback.onTweetsLoaded(tweets));
        }).start();
    }
}
