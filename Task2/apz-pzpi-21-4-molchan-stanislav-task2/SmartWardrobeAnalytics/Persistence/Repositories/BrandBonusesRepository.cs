using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class BrandBonusesRepository(MongoDbContext db) : BaseRepository<BrandBonus>(db, "BrandBonuses"), IBrandBonusesRepository
{
    public async Task<BrandBonus> UpdateAsync(BrandBonus brandBonus, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<BrandBonus>.Update
            .Set(c=> c.ConditionalComplexity, brandBonus.ConditionalComplexity)
            .Set(c => c.ConditionalDiscount, brandBonus.ConditionalDiscount)
            .Set(c=> c.ConditionalNumberOfUsages, brandBonus.ConditionalNumberOfUsages)
            .Set(c => c.LastModifiedDateUtc, brandBonus.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, brandBonus.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<BrandBonus>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<BrandBonus>.Filter.Eq(u => u.Id, brandBonus.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}