package com.juaracoding.utils;

import java.util.List;
import java.util.Map;

public class GenerateStringMenu {

    public String stringMenu(List<Map<String, Object>> lt) {
        StringBuilder sBuilder = new StringBuilder();

        for (Map<String, Object> map : lt) {
            String group = (String) map.get("group");
            List<Map<String, Object>> subMenu = (List<Map<String, Object>>) map.get("subMenu");

            // Mulai dropdown menu
            sBuilder.append("<li class=\"nav-item dropdown\">")
                    .append("<a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"dropdown")
                    .append(group.replaceAll("\\s+", ""))
                    .append("\" role=\"button\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">")
                    .append(group)
                    .append("</a>");

            // Isi dari dropdown menu
            sBuilder.append("<div class=\"dropdown-menu\" aria-labelledby=\"dropdown")
                    .append(group.replaceAll("\\s+", ""))
                    .append("\">");

            for (Map<String, Object> subMenuMap : subMenu) {
                sBuilder.append("<a class=\"dropdown-item\" href=\"")
                        .append(subMenuMap.get("path"))
                        .append("\">")
                        .append(subMenuMap.get("nama"))
                        .append("</a>");
            }

            sBuilder.append("</div>") // penutup dropdown-menu
                    .append("</li>"); // penutup nav-item
        }

        return sBuilder.toString();
    }
}
