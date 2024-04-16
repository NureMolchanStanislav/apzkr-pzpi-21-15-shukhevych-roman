using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class BrandBonusesRepository(MongoDbContext db) : BaseRepository<BrandBonus>(db, "BrandBonuses"), IBrandBonusesRepository
{
    
}