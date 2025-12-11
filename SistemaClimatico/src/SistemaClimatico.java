import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SistemaClimatico {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome da cidade");
        String cidade = sc.nextLine(); // Lê a cidade do teclado.

        try {
            String dadosClimaticos = get.DadosClimaticos(cidade); //Retorna um JSON

            // Código 1006 significa localização não é encontrada.
            if (dadosClimaticos.contains("\"code\":1006")) { // \"code":1006 representa "code":1006.
                System.out.println("Localização não encontrada. Por favor tente novamente.");
            } else {
                imprimirDadosClimaticos(dadosClimaticos);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
