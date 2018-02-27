/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IRCC;

 public class Specimen {

    String id;
    String modelId;
    String strain;
    String implantSite;
    String implantType;
    String passage;

    public Specimen(String id, String modelId, String implantSite, String implantType, String strain, String passage) {
        
        this.id = id;
        this.modelId = modelId;
        this.implantSite = implantSite;
        this.implantType = implantType;
        this.strain = strain;
        this.passage = passage;
    }

    public String getId() {
        return id;
    }

    public String getModelId() {
        return modelId;
    }

    public String getImplantSite() {
        return implantSite;
    }

    public String getImplantType() {
        return implantType;
    }
    
    public String getStrain() {
        return strain;
    }

    public String getPassage() {
        return passage;
    }

}
