using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class OffersRepository(MongoDbContext db) : BaseRepository<Offer>(db, "Offers"), IOffersRepository;