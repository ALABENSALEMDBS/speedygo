using Microsoft.AspNetCore.Mvc;
using ProduitOrder.Models;
using ProduitOrder.Service;

namespace ProduitOrder.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductController : ControllerBase
    {
        private readonly IProductService _service;

        public ProductController(IProductService service)
        {
            _service = service;
        }

        // ✅ Ajouter produit
        [HttpPost("add")]
        public async Task<IActionResult> Add([FromBody] Product product)
        {
            var result = await _service.AddProduct(product);
            return Ok(result);
        }

        // ✅ Modifier produit
        [HttpPut("update")]
        public async Task<IActionResult> Update([FromBody] Product product)
        {
            var result = await _service.UpdateProduct(product);
            return Ok(result);
        }

        // ✅ Supprimer
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            await _service.DeleteProduct(id);
            return Ok("Product deleted");
        }

        // ✅ Obtenir un produit
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(string id)
        {
            var product = await _service.GetProduct(id);
            return product == null ? NotFound() : Ok(product);
        }

        // ✅ Liste produits approuvés
        [HttpGet("approved")]
        public async Task<IActionResult> Approved()
        {
            return Ok(await _service.ListProducts());
        }

        // ✅ Produits par catégorie
        [HttpGet("category/{category}")]
        public async Task<IActionResult> ByCategory(string category)
        {
            return Ok(await _service.GetProductsByCategory(category));
        }

        // ✅ Produits en attente
        [HttpGet("pending")]
        public async Task<IActionResult> Pending()
        {
            return Ok(await _service.GetPendingProducts());
        }

        // ✅ Approuver un produit
        [HttpPut("approve/{id}")]
        public async Task<IActionResult> Approve(string id)
        {
            return Ok(await _service.ApproveProduct(id));
        }

        // ✅ Rejeter un produit
        [HttpPut("reject/{id}")]
        public async Task<IActionResult> Reject(string id)
        {
            return Ok(await _service.RejectProduct(id));
        }

        // ✅ Produits d’un partenaire
        [HttpGet("partner/{partnerName}")]
        public async Task<IActionResult> ProductsForPartner(string partnerName)
        {
            return Ok(await _service.GetProductsForPartner(partnerName));
        }
    }
}
