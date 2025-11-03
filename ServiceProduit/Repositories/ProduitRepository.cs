using MongoDB.Driver;
using ServiceProduit.Models;

namespace ServiceProduit.Repositories
{
    public class ProduitRepository : IProduitRepository
    {
        private readonly IMongoCollection<Produit> _produits;

        public ProduitRepository(IMongoDatabase database)
        {
            _produits = database.GetCollection<Produit>("Produits");
        }

        public async Task<IEnumerable<Produit>> GetAllAsync() =>
            await _produits.Find(_ => true).ToListAsync();

        public async Task<Produit?> GetByIdAsync(string id) =>
            await _produits.Find(p => p.Id == id).FirstOrDefaultAsync();

        public async Task CreateAsync(Produit produit) =>
            await _produits.InsertOneAsync(produit);

        public async Task<bool> UpdateAsync(string id, Produit produit)
        {
            var result = await _produits.ReplaceOneAsync(p => p.Id == id, produit);
            return result.MatchedCount > 0;
        }

        public async Task<bool> DeleteAsync(string id)
        {
            var result = await _produits.DeleteOneAsync(p => p.Id == id);
            return result.DeletedCount > 0;
        }
    }
}
