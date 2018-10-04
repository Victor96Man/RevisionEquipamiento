package com.example.revisionequipamiento.Clases;

import java.util.ArrayList;

public class RevisionObjeto {
    private static RevisionObjeto or;
    int id, usuario,estado,enviado,vp1,vp2,vp3,vp4,vp5,vp6,vp7,vp8,vp9,vp10 = 0;
    String equipamiento,fR,obp1,obp2,obp3,obp4,obp5,obp6,obp7,obp8,obp9,obp10,firma,firmaT,objecione,peticiones = "";
    ArrayList<Foto> fotos;

    public RevisionObjeto(int id, String equipamiento, int usuario, String fR, int estado,int enviado,int vp1,int vp2,int vp3,int vp4,int vp5,int vp6,int vp7,int vp8,int vp9,int vp10,String obp1 ,String obp2 ,String obp3 ,String obp4 ,String obp5 ,String obp6 ,String obp7 ,String obp8 ,String obp9 ,String obp10 ,String firma ,String firmaT ,String objecione ,String peticiones , ArrayList<Foto> fotos) {
        this.id = id;
        this.equipamiento = equipamiento;
        this.usuario = usuario;
        this.fR = fR;
        this.estado = estado;
        this.enviado = enviado;
        this.vp1 = vp1;
        this.vp2 = vp2;
        this.vp3 = vp3;
        this.vp4 = vp4;
        this.vp5 = vp5;
        this.vp6 = vp6;
        this.vp7 = vp7;
        this.vp8 = vp8;
        this.vp9 = vp9;
        this.vp10 = vp10;
        this.obp1 = obp1;
        this.obp2 = obp2;
        this.obp3 = obp3;
        this.obp4 = obp4;
        this.obp5 = obp5;
        this.obp6 = obp6;
        this.obp7 = obp7;
        this.obp8 = obp8;
        this.obp9 = obp9;
        this.obp10 = obp10;
        this.firma = firma;
        this.firmaT = firmaT;
        this.objecione = objecione;
        this.peticiones = peticiones;
        this.fotos = fotos;

    }

    public static RevisionObjeto getObjetoRevision(int id){
        if (or == null) {
            or = new RevisionObjeto(id,"",0,"",0,0,2,2,2,2,2,2,2,2,2,2,"","","","","","","","","","","","","","",null);
        }
        return or;
    }

    public void volveranull(){
        or=null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

    public int getVp1() {
        return vp1;
    }

    public void setVp1(int vp1) {
        this.vp1 = vp1;
    }

    public int getVp2() {
        return vp2;
    }

    public void setVp2(int vp2) {
        this.vp2 = vp2;
    }

    public int getVp3() {
        return vp3;
    }

    public void setVp3(int vp3) {
        this.vp3 = vp3;
    }

    public int getVp4() {
        return vp4;
    }

    public void setVp4(int vp4) {
        this.vp4 = vp4;
    }

    public int getVp5() {
        return vp5;
    }

    public void setVp5(int vp5) {
        this.vp5 = vp5;
    }

    public int getVp6() {
        return vp6;
    }

    public void setVp6(int vp6) {
        this.vp6 = vp6;
    }

    public int getVp7() {
        return vp7;
    }

    public void setVp7(int vp7) {
        this.vp7 = vp7;
    }

    public int getVp8() {
        return vp8;
    }

    public void setVp8(int vp8) {
        this.vp8 = vp8;
    }

    public int getVp9() {
        return vp9;
    }

    public void setVp9(int vp9) {
        this.vp9 = vp9;
    }

    public int getVp10() {
        return vp10;
    }

    public void setVp10(int vp10) {
        this.vp10 = vp10;
    }

    public String getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public String getfR() {
        return fR;
    }

    public void setfR(String fR) {
        this.fR = fR;
    }

    public String getObp1() {
        return obp1;
    }

    public void setObp1(String obp1) {
        this.obp1 = obp1;
    }

    public String getObp2() {
        return obp2;
    }

    public void setObp2(String obp2) {
        this.obp2 = obp2;
    }

    public String getObp3() {
        return obp3;
    }

    public void setObp3(String obp3) {
        this.obp3 = obp3;
    }

    public String getObp4() {
        return obp4;
    }

    public void setObp4(String obp4) {
        this.obp4 = obp4;
    }

    public String getObp5() {
        return obp5;
    }

    public void setObp5(String obp5) {
        this.obp5 = obp5;
    }

    public String getObp6() {
        return obp6;
    }

    public void setObp6(String obp6) {
        this.obp6 = obp6;
    }

    public String getObp7() {
        return obp7;
    }

    public void setObp7(String obp7) {
        this.obp7 = obp7;
    }

    public String getObp8() {
        return obp8;
    }

    public void setObp8(String obp8) {
        this.obp8 = obp8;
    }

    public String getObp9() {
        return obp9;
    }

    public void setObp9(String obp9) {
        this.obp9 = obp9;
    }

    public String getObp10() {
        return obp10;
    }

    public void setObp10(String obp10) {
        this.obp10 = obp10;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getFirmaT() {
        return firmaT;
    }

    public void setFirmaT(String firmaT) {
        this.firmaT = firmaT;
    }

    public String getObjecione() {
        return objecione;
    }

    public void setObjecione(String objecione) {
        this.objecione = objecione;
    }

    public String getPeticiones() {
        return peticiones;
    }

    public void setPeticiones(String peticiones) {
        this.peticiones = peticiones;
    }

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<Foto> fotos) {
        this.fotos = fotos;
    }

   /* @Override
    public String toString() {
        String json ="{'revision':{'id':'"+id+"', 'equipamiento':'"+equipamiento+"', 'usuario':"+usuario+", 'fR':'"+fR+"', 'estado':"+estado+", 'enviado':"+enviado+", 'vp1':"+vp1+", 'vp2':"+vp2+", 'vp3':"+vp3+", 'vp4':"+vp4+", 'vp5':"+vp5+", 'vp6':"+vp6+", 'vp7':"+vp7+", 'vp8':"+vp8+", 'vp9':"+vp9+", 'vp10':"+vp10+", 'obp1':'"+obp1+"', 'obp2':'"+obp2+"', 'obp3':'"+obp3+"', 'obp4':'"+obp4+"', 'obp5':'"+obp5+"', 'obp6':'"+obp6+"', 'obp7':'"+obp7+"', 'obp8':'"+obp8+"', 'obp9':'"+obp9+"', 'obp10':'"+obp10+"', 'firma':'data:image/jpeg;base64,"+firma.replaceAll("\n", "")+"', 'firmaT':'data:image/jpeg;base64,"+firmaT.replaceAll("\n", "")+"', 'objeciones':'"+objecione+"', 'peticiones':'"+peticiones+"','fotos':[";
        if(fotos.size() >0) {
            for (int i = 0; i < fotos.size(); i++) {
                if(i+1 != fotos.size()) {
                    json = json + fotos.get(i).toString()+",";
                }else{
                    json = json + fotos.get(i).toString();
                }
            }
        }else{

        }


        json=json+"]}}";




        return json;
    }*/

    @Override
    public String toString() {
        String json ="{'revision':{'id':'"+id+"', 'equipamiento':'"+equipamiento+"', 'usuario':"+usuario+", 'fR':'"+fR+"', 'estado':"+estado+", 'enviado':"+enviado+", 'vp1':"+vp1+", 'vp2':"+vp2+", 'vp3':"+vp3+", 'vp4':"+vp4+", 'vp5':"+vp5+", 'vp6':"+vp6+", 'vp7':"+vp7+", 'vp8':"+vp8+", 'vp9':"+vp9+", 'vp10':"+vp10+", 'obp1':'"+obp1+"', 'obp2':'"+obp2+"', 'obp3':'"+obp3+"', 'obp4':'"+obp4+"', 'obp5':'"+obp5+"', 'obp6':'"+obp6+"', 'obp7':'"+obp7+"', 'obp8':'"+obp8+"', 'obp9':'"+obp9+"', 'obp10':'"+obp10+"', 'firma':'', 'firmaT':'', 'objeciones':'"+objecione+"', 'peticiones':'"+peticiones+"','fotos':[";
        if(fotos.size() >0) {
            for (int i = 0; i < fotos.size(); i++) {
                if(i+1 != fotos.size()) {
                    json = json + fotos.get(i).toString()+",";
                }else{
                    json = json + fotos.get(i).toString();
                }
            }
        }else{

        }


        json=json+"]}}";




        return json;
    }



}
