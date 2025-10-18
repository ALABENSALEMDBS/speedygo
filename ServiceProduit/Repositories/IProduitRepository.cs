using ServiceProduit.Models;

namespace ServiceProduit.Repositories
{
    public interface IProduitRepository
    {
        Task<IEnumerable<Produit>> GetAllAsync();
        Task<Produit?> GetByIdAsync(string id);
        Task CreateAsync(Produit produit);
        Task<bool> UpdateAsync(string id, Produit produit);
        Task<bool> DeleteAsync(string id);
    }
}
