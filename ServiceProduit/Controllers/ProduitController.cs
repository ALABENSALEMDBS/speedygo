using Microsoft.AspNetCore.Mvc;
using ServiceProduit.Models;
using ServiceProduit.Services;
using Microsoft.AspNetCore.Authorization;

namespace ServiceProduit.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize]
    public class ProduitController : ControllerBase
    {
        private readonly IProduitService _produitService;

        public ProduitController(IProduitService produitService)
        {
            _produitService = produitService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() =>
            Ok(await _produitService.GetAllProduitsAsync());

        [HttpGet("{id:length(24)}")]
        public async Task<IActionResult> GetById(string id)
        {
            var produit = await _produitService.GetProduitByIdAsync(id);
            if (produit == null)
                return NotFound();
            return Ok(produit);
        }

        [HttpPost]
        public async Task<IActionResult> Create(Produit produit)
        {
            try
            {
                var newProduit = await _produitService.AjouterProduitAsync(produit);
                return Ok(newProduit);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPut("{id:length(24)}")]
        public async Task<IActionResult> Update(string id, Produit produit)
        {
            var updated = await _produitService.ModifierProduitAsync(id, produit);
            if (!updated)
                return NotFound();
            return Ok("Produit mis à jour avec succès !");
        }

        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> Delete(string id)
        {
            var deleted = await _produitService.SupprimerProduitAsync(id);
            if (!deleted)
                return NotFound();
            return Ok("Produit supprimé avec succès !");
        }
    }
}
