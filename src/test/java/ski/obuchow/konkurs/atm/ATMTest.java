package ski.obuchow.konkurs.atm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ski.obuchow.konkurs.atm.ATM.Priority;

public class ATMTest {
    private List<ATM> getInput2() {
        List<ATM> atms = new ArrayList<ATM>();
        atms.add(new ATM(4, 1, Priority.STANDARD));
        atms.add(new ATM(1, 1, Priority.STANDARD));
        atms.add(new ATM(2, 1, Priority.STANDARD));
        atms.add(new ATM(3, 2, Priority.PRIORITY));
        atms.add(new ATM(3, 1, Priority.STANDARD));
        atms.add(new ATM(2, 1, Priority.SIGNAL_LOW));
        atms.add(new ATM(5, 2, Priority.STANDARD));
        atms.add(new ATM(5, 1, Priority.FAILURE_RESTART));
        return atms;
    }
    
    private List<ATM> getInput1() {
        List<ATM> atms = new ArrayList<ATM>();
        atms.add(new ATM(1, 2, Priority.STANDARD));
        atms.add(new ATM(1, 1, Priority.STANDARD));
        atms.add(new ATM(2, 3, Priority.PRIORITY));
        atms.add(new ATM(3, 4, Priority.STANDARD));
        atms.add(new ATM(4, 5, Priority.STANDARD));
        atms.add(new ATM(5, 2, Priority.PRIORITY));
        atms.add(new ATM(5, 1, Priority.STANDARD));
        atms.add(new ATM(3, 2, Priority.SIGNAL_LOW));
        atms.add(new ATM(2, 1, Priority.SIGNAL_LOW));
        atms.add(new ATM(3, 1, Priority.FAILURE_RESTART));
        return atms;
    }

    
    private List<ATMBasic> getOutput1() {
        List<ATMBasic> result = new ArrayList<ATMBasic>();
        result.add(new ATMBasic(1, 2));
        result.add(new ATMBasic(1, 1));
        result.add(new ATMBasic(2, 3));
        result.add(new ATMBasic(2, 1));
        result.add(new ATMBasic(3, 1));
        result.add(new ATMBasic(3, 2));
        result.add(new ATMBasic(3, 4));
        result.add(new ATMBasic(4, 5));
        result.add(new ATMBasic(5, 2));
        result.add(new ATMBasic(5, 1));
        return result;
    }
    
    private List<ATMBasic> getOutput2() {
        List<ATMBasic> result = new ArrayList<ATMBasic>();
        result.add(new ATMBasic(1, 1));
        result.add(new ATMBasic(2, 1));
        result.add(new ATMBasic(3, 2));
        result.add(new ATMBasic(3, 1));
        result.add(new ATMBasic(4, 1));
        result.add(new ATMBasic(5, 1));
        result.add(new ATMBasic(5, 2));
        return result;
    }
    
    @Test
    public void solutionLinear1()
    {
        List<ATMBasic> result = ATMApp.solutionLinear(getInput1());
        List<ATMBasic> expected = getOutput1();
        compareOutputs(expected, result);
    }
    
    @Test
    public void solutionLinear2()
    {
        List<ATMBasic> result = ATMApp.solutionLinear(getInput2());
        List<ATMBasic> expected = getOutput2();
        compareOutputs(expected, result);
    }
    
    @Test
    public void solutionSort1()
    {
        List<ATMBasic> result = ATMApp.solutionSort(getInput1());
        List<ATMBasic> expected = getOutput1();
        compareOutputs(expected, result);
    }
    
    @Test
    public void solutionSort2()
    {
        List<ATMBasic> result = ATMApp.solutionSort(getInput2());
        List<ATMBasic> expected = getOutput2();
        compareOutputs(expected, result);
    }

    private void compareOutputs(List<ATMBasic> expected, List<ATMBasic> result) {
        assertEquals(expected.size(), result.size());
        for (int i=0; i<result.size(); i++) {
            assertEquals(expected.get(i).atmId, result.get(i).atmId);
            assertEquals(expected.get(i).region, result.get(i).region);
        }
    }
}
