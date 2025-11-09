namespace ProduitOrder.Dto
{

    public class KeycloakTokenResponse
    {
        //{
        //    [JsonPropertyName("access_token")]
        //    public string AccessToken { get; set; } = string.Empty;

        //    [JsonPropertyName("expires_in")]
        //    public int ExpiresIn { get; set; }
        //}

        //public class KeycloakTokenService
        //{
        //    private readonly HttpClient _httpClient;
        //    private readonly IConfiguration _config;
        //    private string? _cachedToken;
        //    private DateTime _tokenExpiry;

        //    public KeycloakTokenService(HttpClient httpClient, IConfiguration configuration)
        //    {
        //        _httpClient = httpClient;
        //        // Récupère la section S2S
        //        _config = configuration.GetSection("KeycloakS2S");
        //    }

        //    public async Task<string> GetServiceTokenAsync()
        //    {
        //        // Vérifie si le jeton est valide pour au moins la prochaine minute
        //        if (!string.IsNullOrEmpty(_cachedToken) && _tokenExpiry > DateTime.UtcNow.AddMinutes(1))
        //        {
        //            return _cachedToken;
        //        }

        //        // Logique d'obtention de nouveau jeton (Client Credentials Grant)
        //        var content = new FormUrlEncodedContent(new[]
        //        {
        //        new KeyValuePair<string, string>("grant_type", "client_credentials"),
        //        new KeyValuePair<string, string>("client_id", _config["ClientId"]!),
        //        new KeyValuePair<string, string>("client_secret", _config["ClientSecret"]!)
        //    });

        //        // La propriété TokenUrl vient de appsettings.json
        //        var response = await _httpClient.PostAsync(_config["TokenUrl"], content);
        //        response.EnsureSuccessStatusCode();

        //        var tokenResponse = await response.Content.ReadFromJsonAsync<KeycloakTokenResponse>();

        //        // Mise en cache du jeton et de son expiration
        //        _cachedToken = tokenResponse!.AccessToken;
        //        _tokenExpiry = DateTime.UtcNow.AddSeconds(tokenResponse.ExpiresIn);

        //        return _cachedToken;
        //    }
        //}
    }
}
