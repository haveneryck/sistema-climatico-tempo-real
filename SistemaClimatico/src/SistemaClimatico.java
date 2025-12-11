import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class SistemaClimatico {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome da cidade");
        String cidade = sc.nextLine(); // Lê a cidade do teclado.

        try {
            String dadosClimaticos = getDadosClimaticos(cidade); //Retorna um JSON

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

    public static String getDadosClimaticos(String cidade) throws Exception {
        String apiKey = Files.readString( Paths.get("api-key.txt")).trim();

        String formataNomeCidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String apiUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey +"&q=" + formataNomeCidade;
        HttpRequest request = HttpRequest.newBuilder() // Começa a construção de uma nova solicitação HTTP.
                .uri(URI.create(apiUrl)) // Este método define o URI da solicitação HTTP.
                .build();// Finaliza a construção da solicitação HTTP.

        //Criar objeto enviar solicitações HTTP e recever respotas HTTP, para acessar o site da WeatherAPI.
        HttpClient client = HttpClient.newHttpClient();

        // Agora vamos enviar requisições HTTP e receber resposta HTTP, comunicar com o site da API metereológica.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body(); // Retorna os dados metereológicos obtidos no site da API (WheaterAPI).
    }

    // Método para Imprimir os dados metereológicos de forma organizada.
    public static void imprimirDadosClimaticos(String dados) {
        // System.out.println("Dados originais (JSON) obtidos no site metereológico: " + dados);

        JSONObject dadosJson = new JSONObject(dados);
        JSONObject informacoesMetereologicas = dadosJson.getJSONObject("current");

        //Extrai os dados da localização.
        String cidade = dadosJson.getJSONObject("location").getString("name");
        String pais = dadosJson.getJSONObject("location").getString("country");

        // Estrai dados adicionais.
        String condicaoTempo = informacoesMetereologicas.getJSONObject("condition").getString("text");
        int umidade = informacoesMetereologicas.getInt("humidity");
        float velocidadeVEnto = informacoesMetereologicas.getFloat("wind_kph");
        float pressaoAtmosferica = informacoesMetereologicas.getFloat("pressure_mb");
        float sensacaoTermica = informacoesMetereologicas.getFloat("feelslike_c");
        float temperaturaAtual = informacoesMetereologicas.getFloat("temp_c");

        //Extrai a data e a hora da string retornada pela API;
        String daHoraString = informacoesMetereologicas.getString("last_updated");

        // Imprime as informações atuais.
        System.out.println("Informações Metereológicas para " + cidade + ", " + pais);
        System.out.println("Data e Hora: " + daHoraString);
        System.out.println("Temperatura atual: " + temperaturaAtual + "ºC");
        System.out.println("Sensação Térmica: " + sensacaoTermica + "ºC");
        System.out.println("Condição do Tempo: " + condicaoTempo);
        System.out.println("Umidade: " +umidade + "%");
        System.out.println("Velocidade do Vento: " + velocidadeVEnto + "km/h");
        System.out.println("Pressão Atmosférica: " + pressaoAtmosferica + " mb");
    }
}
