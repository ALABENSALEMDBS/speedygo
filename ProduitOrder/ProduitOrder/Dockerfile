FROM mcr.microsoft.com/dotnet/aspnet:8.0

EXPOSE 5001

COPY bin/Release/net8.0/publish/ /app

WORKDIR /app

ENTRYPOINT ["dotnet", "ProduitOrder.dll"]
