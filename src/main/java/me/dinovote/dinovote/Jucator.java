package me.dinovote.dinovote;

public class Jucator {
    private int votes, offlinevotes;
    private long lastvoted;
    private String nume;
    public Jucator(){
        this.votes = 0;
        this.offlinevotes = 0;
        this.nume = null;
        this.lastvoted = 0;
    }

    public void setVotes(int votes){
        this.votes = votes;
    }

    public int getVotes(){
        return this.votes;
    }

    public void addVote(){
        this.votes = this.votes + 1;
        this.lastvoted = System.currentTimeMillis();
    }

    public void setOfflinevotes(int offlinevotes){
        this.offlinevotes = offlinevotes;
    }

    public int getOfflineVotes(){
        return this.offlinevotes;
    }

    public void addOfflineVote(){
        this.offlinevotes = this.offlinevotes + 1;
    }

    public String getNume(){
        return this.nume;
    }

    public void setNume(String nume){
        this.nume = nume;
    }

    public long getLastVoted(){
        return this.lastvoted;
    }

    public void setLastVoted(long lastvoted){
        this.lastvoted = lastvoted;
    }

    public void setData(String nume, int votes, int offlinevotes, long lastvoted){
        this.nume = nume;
        this.votes = votes;
        this.offlinevotes = offlinevotes;
        this.lastvoted = lastvoted;
    }
}
