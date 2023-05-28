package knyazev.model;

import knyazev.dao.IdentityInterface;

import java.util.ArrayList;
import java.util.List;

public class ElectricalCircuit implements IdentityInterface<Long> {
    private List<SubCircuit> subCircuits;
    private Long identity;

    public ElectricalCircuit() {
        subCircuits = new ArrayList<>();
    }

    public List<SubCircuit> getSubCircuits() {
        return subCircuits;
    }

    public void setSubCircuits(List<SubCircuit> lanes) {
        this.subCircuits = lanes;
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
        return "ElectricalCircuit{" +
                "subCircuits=" + subCircuits +
                ", identity=" + identity +
                '}';
    }
}
