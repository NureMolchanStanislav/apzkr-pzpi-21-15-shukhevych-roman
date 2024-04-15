using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class BrandsRepository(MongoDbContext db) : BaseRepository<Brand>(db, "Brands"), IBrandsRepository;