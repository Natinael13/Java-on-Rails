package jrails;

public class Html {

    String stringform;

    public String toString() {
        return stringform;
    }

    public Html seq(Html h) {
        Html newHtml = new Html();
        newHtml.stringform = this.stringform + h.toString();
        return newHtml;
    }

    public Html br() {
        Html newHtml = new Html();
        newHtml.stringform = "<br/>";
        return newHtml;
    }

    public Html t(Object o) {
        Html newHtml = new Html();
        newHtml.stringform = o.toString();
        return newHtml;
    }

    public Html p(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<p>" + child.toString() +"</p>";
        return newHtml;
    }

    public Html div(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<div>" + child.toString() + "</div>";
        return newHtml;
    }

    public Html strong(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<strong>" + child.toString() + "</strong>";
        return newHtml;
    }

    public Html h1(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<h1>" + child.toString() + "</h1>";
        return newHtml;
    }

    public Html tr(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<tr>" + child.toString() + "</tr>";
        return newHtml;
    }

    public Html th(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<th>" + child.toString() + "</th>";
        return newHtml;
    }

    public Html td(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<td>" + child.toString() + "</td>";
        return newHtml;
    }

    public Html table(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<table>" + child.toString() + "</table>";
        return newHtml;
    }

    public Html thead(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<thead>" + child.toString() + "</thead>";
        return newHtml;
    }

    public Html tbody(Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<tbody>" + child.toString() + "</tbody>";
        return newHtml;
    }

    public Html textarea(String name, Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<textarea name=" + '"' + name + '"' + ">" + child.toString() + "</textarea>";
        return newHtml;
    }

    public Html link_to(String text, String url) {
        Html newHtml = new Html();
        newHtml.stringform = "<a href=" + '"' + url + '"' + ">" + text + "</a>";
        return newHtml;
    }

    public Html form(String action, Html child) {
        Html newHtml = new Html();
        newHtml.stringform = "<form action=" + '"' + action + '"' + " accept-charset=" + '"' + "UTF-8" + '"' + " method=" + '"' + "post" + '"' + ">" + child.toString() + "</form>";
        return newHtml;
    }

    public Html submit(String value) {
        Html newHtml = new Html();
        newHtml.stringform = "<input type=" + '"' + "submit" + '"' + " value=" + '"' + value + '"' + "/>";
        return newHtml;
    }
}