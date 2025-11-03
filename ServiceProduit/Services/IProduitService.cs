using ServiceProduit.Models;

namespace ServiceProduit.Services
{
    public interface IProduitService
    {
        Task<IEnumerable<Produit>> GetAllProduitsAsync();
        Task<Produit?> GetProduitByIdAsync(string id);
        Task<Produit> AjouterProduitAsync(Produit produit);
        Task<bool> ModifierProduitAsync(string id, Produit produit);
        Task<bool> SupprimerProduitAsync(string id);
    }
}
