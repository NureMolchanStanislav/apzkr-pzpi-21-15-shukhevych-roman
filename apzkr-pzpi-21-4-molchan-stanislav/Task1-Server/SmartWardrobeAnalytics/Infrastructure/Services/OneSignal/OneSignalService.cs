using System.Net.Http.Json;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;

namespace Infrastructure.Services.OneSignal;

public class OneSignalService
{
    private readonly string _appId;
    private readonly string _apiKey;

    public OneSignalService(IConfiguration configuration)
    {
        _appId = configuration["OneSignal:AppId"];
        _apiKey = configuration["OneSignal:ApiKey"];
    }

    public async Task<bool> SendNotificationAsync(string message, string heading)
    {
        using (var client = new HttpClient())
        {
            client.DefaultRequestHeaders.Add("Authorization", $"Basic {_apiKey}");

            var payload = new
            {
                app_id = _appId,
                included_segments = new[] { "All" },
                headings = new { en = heading },
                contents = new { en = message }
            };

            var jsonPayload = JsonConvert.SerializeObject(payload);
            Console.WriteLine($"JSON Payload: {jsonPayload}");

            var response = await client.PostAsync("https://onesignal.com/api/v1/notifications", new StringContent(jsonPayload, System.Text.Encoding.UTF8, "application/json"));

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error: {errorContent}");
            }

            return response.IsSuccessStatusCode;
        }
    }
}