# project_9

Gruppe 9:
- Philipp Pedron ic23b007@technikum-wien.at
- Fiona Wallensteiner ic23b068@technikum-wien.at

## Weather Dashboard with JavaFX

Dieses Projekt implementiert ein Echtzeit-Wetter-Dashboard mit JavaFX, das Wetterdaten über die OpenWeatherMap API abruft und dem Benutzer in einer grafischen Oberfläche anzeigt.

---

## **Projektbeschreibung**
Das Ziel dieses Projekts ist es, ein benutzerfreundliches Wetter-Dashboard zu erstellen, das Echtzeit-Wetterdaten für verschiedene Städte anzeigt. Die Anwendung verwendet JavaFX für die GUI und die OpenWeatherMap API für den Datenabruf. Benutzer können zwischen verschiedenen Temperatureinheiten wählen, Wetterdaten für unterschiedliche Städte abrufen und bevorzugte Städte speichern.

---

## **Features**

### **Must-Have Features**
- **Abruf von Wetterdaten über die OpenWeatherMap API**:
    - Anzeigen der aktuellen Temperatur, Luftfeuchtigkeit, Windgeschwindigkeit und des Wetterzustands.
- **JavaFX GUI**:
    - Klar strukturierte Oberfläche zur Anzeige der Wetterdaten.
    - Automatische Aktualisierung der Wetterinformationen.

### **Should-Have Features**
- **Maßeinheiten umschalten**:
    - Wechsel zwischen Celsius und Fahrenheit.
- **Standortwechsel**:
    - Abrufen und Anzeigen von Wetterdaten für verschiedene Städte.

### **Nice-to-Have Features**
- **Speichern bevorzugter Städte**:
    - Benutzer können eine Liste bevorzugter Städte anlegen und schnell auf diese zugreifen.
- **Warnmeldungen bei extremen Wetterbedingungen**:
    - Die Anwendung zeigt Warnmeldungen bei extremen Sturm- oder Wetterbedingungen an, damit der Benutzer gewarnt wird.

---

## **Technologien**
- **Java** (Version 11 oder höher): Programmiersprache für die Backend-Logik und GUI.
- **JavaFX**: Framework für die grafische Benutzeroberfläche.
- **OpenWeatherMap API**: Externe API zum Abrufen von Echtzeit-Wetterdaten.
- **Maven**: Build-Management-Tool für Abhängigkeiten und Projekterstellung.
- **Lombok**: Für vereinfachung vom Code (z. B. Getter/Setter)
- **Gson**: Zum Parsen von JSON-Daten

--- 

### **Anwendung starten**
- **Server starten**:
    - Führe die `TCPServer`-Klasse aus.
- **Client starten**:
    - Starte die `HelloApplication`-Klasse für die GUI.

    1. **Stadt suchen**:
        - Geben Sie einen Städtenamen im Suchfeld ein und klicken Sie auf „Search“.
    2. **Favoriten hinzufügen**:
        - Speichern Sie häufig gesuchte Städte als Favoriten.
    3. **Favoriten verwenden**:
        - Wählen Sie gespeicherte Städte direkt aus der Dropdown-Liste aus.
    4. **Einheiten wechseln**:
        - Wählen Sie zwischen metrischen (°C, m/s) und imperialen (°F, mph) Einheiten.

---

### **Wichtige Klassen und ihre Funktionen**
- **FavoritesManager**:
    - Verwalten der Lieblingsstädte und deren Speicherung in `favorites.log`.
- **Logger**:
    - Protokollierung von Debug-, Info- und Fehlernachrichten in `application.log`.
- **TCPClient**:
    - Senden von Anfragen und Empfangen von Daten vom Server.
- **TCPServer**:
    - Abrufen der Wetterdaten von der OpenWeatherMap API und Weiterleitung an den Client.
- **WeatherParser**:
    - Parsen von JSON-Daten und Konvertierung in das `WeatherData`-Modell.

 

---
