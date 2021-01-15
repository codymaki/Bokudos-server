package com.bokudos.bokudosserver.entities;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class AbstractObject {
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    protected double width;
    protected double height;
}
