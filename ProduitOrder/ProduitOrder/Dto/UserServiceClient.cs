using ProduitOrder.Dto;
using Steeltoe.Discovery;
using System.Net.Http.Headers;

public class UserServiceClient
{
   

    private readonly HttpClient _httpClient;
    private readonly IDiscoveryClient _discoveryClient;
    private readonly IHttpContextAccessor _httpContextAccessor;

    public UserServiceClient(HttpClient httpClient, IDiscoveryClient discoveryClient, IHttpContextAccessor httpContextAccessor)
    {
        _httpClient = httpClient;
        _discoveryClient = discoveryClient;

        _httpClient.DefaultRequestHeaders.Accept.Clear();
        _httpClient.DefaultRequestHeaders.Accept.Add(
            new MediaTypeWithQualityHeaderValue("application/json"));
        _httpContextAccessor = httpContextAccessor;
    }

   


    public async Task<UserDTO?> GetUserByIdAsync(long userId)
    {
        try
        {
            // Get current token from HTTP context
            var token = _httpContextAccessor.HttpContext?.Request.Headers["Authorization"]
                          .FirstOrDefault()?.Replace("Bearer ", "");

            // Set the token in the HttpClient
            _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);

            Console.WriteLine($"🔍 USER SERVICE CLIENT: Starting lookup for user {userId}");

            // Test 1: Direct access to UserService
            var directUrl = $"http://localhost:9999/api/users/{userId}";
            Console.WriteLine($"🔍 USER SERVICE CLIENT: Testing direct URL: {directUrl}");

            var directResponse = await _httpClient.GetAsync(directUrl);
            Console.WriteLine($"🔍 USER SERVICE CLIENT: Direct Response Status: {directResponse.StatusCode}");

            if (directResponse.IsSuccessStatusCode)
            {
                var user = await directResponse.Content.ReadFromJsonAsync<UserDTO>();
                Console.WriteLine($"✅ USER SERVICE CLIENT: User found via direct: {user?.FirstName} {user?.LastName}");
                return user;
            }
            else
            {
                var errorContent = await directResponse.Content.ReadAsStringAsync();
                Console.WriteLine($"❌ USER SERVICE CLIENT: Direct Error: {errorContent}");
            }

            // Test 2: Via API Gateway
            var gatewayUrl = $"http://localhost:8765/api/users/{userId}";
            Console.WriteLine($"🔍 USER SERVICE CLIENT: Testing gateway URL: {gatewayUrl}");

            var gatewayResponse = await _httpClient.GetAsync(gatewayUrl);
            Console.WriteLine($"🔍 USER SERVICE CLIENT: Gateway Response Status: {gatewayResponse.StatusCode}");

            if (gatewayResponse.IsSuccessStatusCode)
            {
                var user = await gatewayResponse.Content.ReadFromJsonAsync<UserDTO>();
                Console.WriteLine($"✅ USER SERVICE CLIENT: User found via gateway: {user?.FirstName} {user?.LastName}");
                return user;
            }
            else
            {
                var errorContent = await gatewayResponse.Content.ReadAsStringAsync();
                Console.WriteLine($"❌ USER SERVICE CLIENT: Gateway Error: {errorContent}");
            }

            return null;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"💥 USER SERVICE CLIENT: Exception: {ex.Message}");
            Console.WriteLine($"💥 USER SERVICE CLIENT: StackTrace: {ex.StackTrace}");
            return null;
        }
    }
}