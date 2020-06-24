package ca.qc.johnabbott.cs616.server.model;

public enum AstrologicalSign {
    Aries(1),Taurus(2),Gemini(3),Cancer(4),Leo(5),Virgo(6),Libra(7),Scorpio(8),Sagittarius(8),Capricorn(10),Aquarius(11),Pisces(12);

    private int AstrologicalId;

    AstrologicalSign(int AstrologicalId){
        this.AstrologicalId=AstrologicalId;
    }

    //Return the specific astrological sign
    public int getAstrologicalId(){
        return AstrologicalId;
    }

}
