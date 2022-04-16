/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author celio
 */
public class DiaTest {
    
    public DiaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCodDia method, of class Dia.
     */
    @Test
    public void testGetCodDia() {
        System.out.println("getCodDia");
        Dia instance = new Dia();
        String expResult = "";
        String result = instance.getCodDia();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCodDia method, of class Dia.
     */
    @Test
    public void testSetCodDia() {
        System.out.println("setCodDia");
        String codDia = "";
        Dia instance = new Dia();
        instance.setCodDia(codDia);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSaldo_anterior method, of class Dia.
     */
    @Test
    public void testGetSaldo_anterior() {
        System.out.println("getSaldo_anterior");
        Dia instance = new Dia();
        String expResult = "";
        String result = instance.getSaldo_anterior();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSaldo_anterior method, of class Dia.
     */
    @Test
    public void testSetSaldo_anterior() {
        System.out.println("setSaldo_anterior");
        String saldo_anterior = "";
        Dia instance = new Dia();
        instance.setSaldo_anterior(saldo_anterior);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDia_mes method, of class Dia.
     */
    @Test
    public void testGetDia_mes() {
        System.out.println("getDia_mes");
        Dia instance = new Dia();
        String expResult = "";
        String result = instance.getDia_mes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDia_mes method, of class Dia.
     */
    @Test
    public void testSetDia_mes() {
        System.out.println("setDia_mes");
        String dia_mes = "";
        Dia instance = new Dia();
        instance.setDia_mes(dia_mes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSaldo_atual method, of class Dia.
     */
    @Test
    public void testGetSaldo_atual() {
        System.out.println("getSaldo_atual");
        Dia instance = new Dia();
        String expResult = "";
        String result = instance.getSaldo_atual();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSaldo_atual method, of class Dia.
     */
    @Test
    public void testSetSaldo_atual() {
        System.out.println("setSaldo_atual");
        String saldo_atual = "";
        Dia instance = new Dia();
        instance.setSaldo_atual(saldo_atual);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSituacao method, of class Dia.
     */
    @Test
    public void testGetSituacao() {
        System.out.println("getSituacao");
        Dia instance = new Dia();
        String expResult = "";
        String result = instance.getSituacao();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSituacao method, of class Dia.
     */
    @Test
    public void testSetSituacao() {
        System.out.println("setSituacao");
        String situacao = "";
        Dia instance = new Dia();
        instance.setSituacao(situacao);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
