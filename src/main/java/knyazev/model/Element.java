package knyazev.model;
import knyazev.dao.IdentityInterface;

public class Element implements IdentityInterface<Long> {

    private int amperage;
    private int resistance;
    private ElementType type;
    private Long identity;

    public Element() {}

    public int getAmperage() {
        return amperage;
    }

    public void setAmperage(int amperage) {
        this.amperage = amperage;
    }

    public int getResistance() {
        return resistance;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    @Override
    public Long getIdentity() {
        return identity;
    }

    @Override
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "Element{" +
                "amperage=" + amperage +
                ", resistance=" + resistance +
                ", type=" + type +
                ", identity=" + identity +
                '}';
    }
}
