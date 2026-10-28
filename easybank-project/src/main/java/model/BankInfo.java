package model;
public final class BankInfo {
    private final String name;
    private final String branch;
    public BankInfo(String name, String branch) {
        this.name = name;
        this.branch = branch;
    }
    public String name() { return name; }
    public String branch() { return branch; }
    @Override public String toString() { return name + " | Branch: " + branch; }
}