package org.docencia.hotel.model.nosql;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@Document(collection = "guest_preferences")
public class GuestPreferences {
    @Id
    private String id; 
    private String guestId;
    private String preferredLanguage;
    private boolean newsletterOptIn;
    private String favoriteRoomType;
    private String[] tags;
    private String notes;
    

    public GuestPreferences() {
    }

    public GuestPreferences(String id, String guestId, String preferredLanguage, boolean newsletterOptIn, String favoriteRoomType, String[] tags, String notes) {
        this.id = id;
        this.guestId = guestId;
        this.preferredLanguage = preferredLanguage;
        this.newsletterOptIn = newsletterOptIn;
        this.favoriteRoomType = favoriteRoomType;
        this.tags = tags;
        this.notes = notes;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuestId() {
        return this.guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getPreferredLanguage() {
        return this.preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public boolean isNewsletterOptIn() {
        return this.newsletterOptIn;
    }

    public boolean getNewsletterOptIn() {
        return this.newsletterOptIn;
    }

    public void setNewsletterOptIn(boolean newsletterOptIn) {
        this.newsletterOptIn = newsletterOptIn;
    }

    public String getFavoriteRoomType() {
        return this.favoriteRoomType;
    }

    public void setFavoriteRoomType(String favoriteRoomType) {
        this.favoriteRoomType = favoriteRoomType;
    }

    public String[] getTags() {
        return this.tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GuestPreferences)) {
            return false;
        }
        GuestPreferences guestPreferences = (GuestPreferences) o;
        return Objects.equals(id, guestPreferences.id) && Objects.equals(guestId, guestPreferences.guestId) && Objects.equals(preferredLanguage, guestPreferences.preferredLanguage) && newsletterOptIn == guestPreferences.newsletterOptIn && Objects.equals(favoriteRoomType, guestPreferences.favoriteRoomType) && Objects.equals(tags, guestPreferences.tags) && Objects.equals(notes, guestPreferences.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guestId, preferredLanguage, newsletterOptIn, favoriteRoomType, tags, notes);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", guestId='" + getGuestId() + "'" +
            ", preferredLanguage='" + getPreferredLanguage() + "'" +
            ", newsletterOptIn='" + isNewsletterOptIn() + "'" +
            ", favoriteRoomType='" + getFavoriteRoomType() + "'" +
            ", tags='" + getTags() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }

}