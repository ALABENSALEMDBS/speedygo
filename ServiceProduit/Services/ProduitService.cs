using ServiceProduit.Models;
using ServiceProduit.Repositories;

namespace ServiceProduit.Services
{
    public class ProduitService : IProduitService
    {
        private readonly IProduitRepository _repository;

        public ProduitService(IProduitRepository repository)
        {
            _repository = repository;
        }

        public async Task<IEnumerable<Produit>> GetAllProduitsAsync()
        {
            return await _repository.GetAllAsync();
        }

        public async Task<Produit?> GetProduitByIdAsync(string id)
        {
            return await _repository.GetByIdAsync(id);
        }

        public async Task<Produit> AjouterProduitAsync(Produit produit)
        {
            // 💡 Exemple de logique métier : prix doit être positif
            if (produit.Prix <= 0)
                throw new ArgumentException("Le prix du produit doit être supérieur à 0.");

            await _repository.CreateAsync(produit);
            return produit;
        }

        public async Task<bool> ModifierProduitAsync(string id, Produit produit)
        {
            var existing = await _repository.GetByIdAsync(id);
            if (existing == null)
                return false;

            return await _repository.UpdateAsync(id, produit);
        }

        public async Task<bool> SupprimerProduitAsync(string id)
        {
            return await _repository.DeleteAsync(id);
        }
    }
}
