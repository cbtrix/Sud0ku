package br.com.dio.model;

import java.util.Collection;
import java.util.List;
import javax.swing.Timer;
import java.awt.Color;

import static br.com.dio.model.GameStatusEnum.COMPLETE;
import static br.com.dio.model.GameStatusEnum.INCOMPLETE;
import static br.com.dio.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus(){
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public void animateSpace(int row, int col) {
        System.out.println("✨ Número colocado na posição [" + row + "," + col + "]!");

        // Piscar no console
        Timer timer = new Timer(200, null);
        final int[] count = {0};

        timer.addActionListener(e -> {
            if (count[0] < 3) {
                System.out.println("💫 BLINK!");
                count[0]++;
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }
    public boolean hasErrors(){
        if(getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }

}
