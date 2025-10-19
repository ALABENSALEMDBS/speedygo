//var builder = WebApplication.CreateBuilder(args);

//// Add services to the container.
//// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
//builder.Services.AddOpenApi();

//var app = builder.Build();

//// Configure the HTTP request pipeline.
//if (app.Environment.IsDevelopment())
//{
//    app.MapOpenApi();
//}

//app.UseHttpsRedirection();

//var summaries = new[]
//{
//    "Freezing", "Bracing", "Chilly", "Cool", "Mild", "Warm", "Balmy", "Hot", "Sweltering", "Scorching"
//};

//app.MapGet("/weatherforecast", () =>
//{
//    var forecast =  Enumerable.Range(1, 5).Select(index =>
//        new WeatherForecast
//        (
//            DateOnly.FromDateTime(DateTime.Now.AddDays(index)),
//            Random.Shared.Next(-20, 55),
//            summaries[Random.Shared.Next(summaries.Length)]
//        ))
//        .ToArray();
//    return forecast;
//})
//.WithName("GetWeatherForecast");

//app.Run();

//record WeatherForecast(DateOnly Date, int TemperatureC, string? Summary)
//{
//    public int TemperatureF => 32 + (int)(TemperatureC / 0.5556);
//}

using Microsoft.OpenApi.Models;

using MongoDB.Driver;
using Steeltoe.Discovery.Eureka;
//using Steeltoe.Discovery.ClientBase;
using ServiceProduit.Repositories;
using ServiceProduit.Services;

var builder = WebApplication.CreateBuilder(args);

// Eureka Client
builder.Services.AddServiceDiscovery(options =>
{
    options.UseEureka();
});
// MongoDB Config
builder.Services.AddSingleton<IMongoClient>(sp =>
{
    var connectionString = builder.Configuration["MongoDB:ConnectionString"];
    return new MongoClient(connectionString);
});

builder.Services.AddSingleton(sp =>
{
    var client = sp.GetRequiredService<IMongoClient>();
    var dbName = builder.Configuration["MongoDB:DatabaseName"];
    return client.GetDatabase(dbName);
});


builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "ServiceProduit API", Version = "v1" });
});

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "ServiceProduit API V1");
    });
}


// Dépendances
builder.Services.AddScoped<IProduitRepository, ProduitRepository>();
builder.Services.AddScoped<IProduitService, ProduitService>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();
app.UseSwagger();
app.UseSwaggerUI();
app.MapControllers();

app.Run();

