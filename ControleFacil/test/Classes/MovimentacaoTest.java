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
public class MovimentacaoTest {
    
    public MovimentacaoTest() {
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
     * Test of getCodMovimentacao method, of class Movimentacao.
     */
    @Test
    public void testGetCodMovimentacao() {
        System.out.println("getCodMovimentacao");
        Movimentacao instance = new Movimentacao();
        String expResult = "";
        String result = instance.getCodMovimentacao();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCodMovimentacao method, of class Movimentacao.
     */
    @Test
    public void testSetCodMovimentacao() {
        System.out.println("setCodMovimentacao");
        String codMovimentacao = "";
        Movimentacao instance = new Movimentacao();
        instance.setCodMovimentacao(codMovimentacao);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTipo method, of class Movimentacao.
     */
    @Test
    public void testGetTipo() {
        System.out.println("getTipo");
        Movimentacao instance = new Movimentacao();
        String expResult = "";
        String result = instance.getTipo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTipo method, of class Movimentacao.
     */
    @Test
    public void testSetTipo() {
        System.out.println("setTipo");
        String tipo = "";
        Movimentacao instance = new Movimentacao();
        instance.setTipo(tipo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class Movimentacao.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        Movimentacao instance = new Movimentacao();
        String expResult = "";
        String result = instance.getData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class Movimentacao.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        String data = "";
        Movimentacao instance = new Movimentacao();
        instance.setData(data);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValor method, of class Movimentacao.
     */
    @Test
    public void testGetValor() {
        System.out.println("getValor");
        Movimentacao instance = new Movimentacao();
        String expResult = "";
        String result = instance.getValor();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValor method, of class Movimentacao.
     */
    @Test
    public void testSetValor() {
        System.out.println("setValor");
        String valor = "";
        Movimentacao instance = new Movimentacao();
        instance.setValor(valor);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescricao method, of class Movimentacao.
     */
    @Test
    public void testGetDescricao() {
        System.out.println("getDescricao");
        Movimentacao instance = new Movimentacao();
        String expResult = "";
        String result = instance.getDescricao();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDescricao method, of class Movimentacao.
     */
    @Test
    public void testSetDescricao() {
        System.out.println("setDescricao");
        String descricao = "";
        Movimentacao instance = new Movimentacao();
        instance.setDescricao(descricao);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
