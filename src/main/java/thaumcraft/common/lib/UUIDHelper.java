package thaumcraft.common.lib;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDHelper {
   public static String uuidToName(String uuid) {
      if (!uuid.isEmpty()) {
         try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + cleanUUID(uuid));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            JsonObject profile = (JsonObject)(new JsonParser()).parse(isr);
            return profile.get("name").toString().replace('"', '\u0000').trim();
         } catch (Exception ignored) {
         }
      }

      return "";
   }

   public static UUID getOnlineUUID(String name) {
      if (!Strings.isNullOrEmpty(name)) {
         try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            JsonObject profile = (JsonObject)(new JsonParser()).parse(new InputStreamReader(connection.getInputStream()));
            return UUID.fromString(fullUUID(profile.get("id").toString()));
         } catch (Exception ignored) {
         }
      }

      return null;
   }

   public static UUID getOfflineUUID(String name) {
      return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
   }

   public static UUID stringToUUID(String uuid) {
      try {
         return UUID.fromString(uuid);
      } catch (Exception var2) {
         return null;
      }
   }

   public static String fullUUID(String uuid) {
      uuid = cleanUUID(uuid);
      uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
      return uuid;
   }

   public static String cleanUUID(String uuid) {
      return uuid.replaceAll("[^a-zA-Z0-9]", "");
   }
}
