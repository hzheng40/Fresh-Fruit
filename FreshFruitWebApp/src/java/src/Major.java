package src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Benjamin
 */
public enum Major {

    AE("Aerospace Engineering"),
    ARCH("Architecture"),
    BME("Biomedical Engineering"),
    ChemE("Chemical Engineering"),
    Chem("Chemistry"),
    CM("Computational Media"),
    CS("Computer Science"),
    EE("Electrical Engineering"),
    ISYE("Industrial and Systems Engineering"),
    Math("Mathematics"),
    MGT("Management"),
    ME("Mechanical Engineering"),
    Phys("Physics"),
    Un("Undecided");

    /**
     * Full name of a major.
     */
    public String fullName;

    /**
     * Sets major.
     * @param name the name of the major
     */
    private Major(String name) {
        fullName = name;
    }

    /**
     * Returns the full name of the major.
     * @return the full name of the major
     */
    public String getFullName() {
        return fullName;
    }

    /**
    * Gets Major enum from a full name string.
    * @param fullName get the name of major
    * @return the Major enum corresponding to the string
    */
    public static Major getMajorFromString(String fullName) {
        try {
            for (Major m : Major.values()) {
                if (m.fullName.equals(fullName)) {
                    return m;
                }
            }
        } catch (IllegalArgumentException e) {
            return Major.Un;
        }
        return Major.Un;
    }
}