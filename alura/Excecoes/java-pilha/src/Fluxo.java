public class Fluxo {

  public static void main(String[] args) {
    System.out.println("Ini do main");
    try {
      metodo1();
    } catch (NullPointerException | ArithmeticException | MinhaExcecao ex) {
      System.out.println("exception do try : " + ex.getMessage());
      ex.printStackTrace();
    }
    System.out.println("Fim do main");
  }

  private static void metodo1() throws MinhaExcecao {
    System.out.println("Ini do metodo1");
    try {
      metodo2();
    } catch (MinhaExcecao ex) {

    }
    System.out.println("Fim do metodo1");
  }

  private static void metodo2() throws MinhaExcecao {
    System.out.println("Ini do metodo2");
    Conta c = new Conta();
    throw new MinhaExcecao("F");

    //System.out.println("Fim do metodo2");
  }
}