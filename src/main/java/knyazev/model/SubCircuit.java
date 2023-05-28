package knyazev.model;

import knyazev.dao.IdentityInterface;

import java.util.ArrayList;
import java.util.List;

public class SubCircuit implements IdentityInterface<Long> {
    private List<Element> elements;
    private Long identity;

    public SubCircuit() {
        elements = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
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
        return "SubCircuit{" +
                "elements=" + elements +
                ", identity=" + identity +
                '}';
    }
}
