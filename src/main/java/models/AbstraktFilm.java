package models;

import java.sql.Date;
import java.util.List;

public class AbstraktFilm {

    private long ID = -1; //-1 means no id yet => not saved in database

    private Produksjonsselskap produksjonsselskap;
    private String tittel;
    private int lengde;
    private int utgivelsesar;
    private Date langeringsDato;
    private String beskrivelse;
    private int opprinneligLagetFor;

    private List<Kategori> categoryList;

    public AbstraktFilm(int id){
        this.ID = id;
    }

    public AbstraktFilm(Produksjonsselskap produksjonsselskap, String tittel, int lengde,
                int utgivelsesar, Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        this.produksjonsselskap = produksjonsselskap;
        this.tittel = tittel;
        this.lengde = lengde;
        this.utgivelsesar = utgivelsesar;
        this.langeringsDato = langeringsDato;
        this.beskrivelse = beskrivelse;
        this.opprinneligLagetFor = opprinneligLagetFor;
    }

    public void initAbstraktFilmValues(Produksjonsselskap produksjonsselskap, String tittel, int lengde,
                int utgivelsesar, Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        this.produksjonsselskap = produksjonsselskap;
        this.tittel = tittel;
        this.lengde = lengde;
        this.utgivelsesar = utgivelsesar;
        this.langeringsDato = langeringsDato;
        this.beskrivelse = beskrivelse;
        this.opprinneligLagetFor = opprinneligLagetFor;
    }

    public long getID() {
        return ID;
    }

    public Produksjonsselskap getProduksjonsselskap() {
        return produksjonsselskap;
    }

    public String getTittel() {
        return tittel;
    }

    public int getLengde() {
        return lengde;
    }

    public int getUtgivelsesar() {
        return utgivelsesar;
    }

    public Date getLangeringsDato() {
        return langeringsDato;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public int getOpprinneligLagetFor() {
        return opprinneligLagetFor;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
